package edu.scripps.yates.annotations.uniprot.index;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.nio.channels.FileLock;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.xml.bind.JAXB;
import javax.xml.bind.JAXBException;

import org.apache.commons.io.FilenameUtils;
import org.apache.log4j.Logger;

import edu.scripps.yates.annotations.util.IndexException;
import edu.scripps.yates.utilities.annotations.uniprot.UniprotEntryUtil;
import edu.scripps.yates.utilities.annotations.uniprot.xml.Entry;
import edu.scripps.yates.utilities.dates.DatesUtil;
import edu.scripps.yates.utilities.index.FileIndex;
import edu.scripps.yates.utilities.util.Pair;
import gnu.trove.map.hash.THashMap;

public class UniprotXmlIndex implements FileIndex<Entry> {
	private static final Logger log = Logger.getLogger(UniprotXmlIndex.class);
	private final File fileToIndex;
	private final File indexFile;
	protected final Map<String, Pair<Long, Long>> indexMap = new THashMap<>();
	private final static String INDEX_EXT = ".idx";
	private static final String TAB = "\t";
	private static final String NEWLINE = "\n";
	protected final UniprotXmlIndexIO uniprotFileIndexIO;
	private final boolean ignoreReferences;
	private final boolean ignoreDBReferences;
	private final static int MAX_NUMBER_ENTRIES_PER_PAIR = 100;

	private enum Status {
		READY, NOT_READY
	};

	private Status status = Status.NOT_READY;

	public UniprotXmlIndex(File file, boolean ignoreReferences, boolean ignoreDBReferences)
			throws IOException, JAXBException {
		if (!file.exists()) {
			file.getParentFile().mkdirs();
		}
		fileToIndex = file;
		// create the index file
		indexFile = new File(getIndexPathName(file));
		uniprotFileIndexIO = new UniprotXmlIndexIO(fileToIndex);
		this.ignoreReferences = ignoreReferences;
		this.ignoreDBReferences = ignoreDBReferences;
	}

	private String getIndexPathName(File file) {
		final String pathName = file.getParent() + File.separator + FilenameUtils.getBaseName(file.getAbsolutePath())
				+ INDEX_EXT;
		return pathName;
	}

	public UniprotXmlIndex(String path, boolean ignoreReferences, boolean ignoreDBReferences)
			throws IOException, JAXBException {
		this(new File(path), ignoreReferences, ignoreDBReferences);
	}

	private void indexFile() throws IOException {
		log.info("Indexing file " + FilenameUtils.getName(fileToIndex.getAbsolutePath()) + "...");
		final long t1 = System.currentTimeMillis();
		// read the index, getting the positions of the items
		final Map<String, Pair<Long, Long>> indexMap = uniprotFileIndexIO.getIndexMap();
		// add to the map
		this.indexMap.putAll(indexMap);
		final long t2 = System.currentTimeMillis();
		// write the index file without appending
		writePositionsInIndex(this, indexMap, false);
		final long t3 = System.currentTimeMillis();

		log.info(DatesUtil.getDescriptiveTimeFromMillisecs(t2 - t1) + " to read the input file");
		log.info(DatesUtil.getDescriptiveTimeFromMillisecs(t3 - t2) + " to write the index to "
				+ indexFile.getAbsolutePath());
	}

	private static synchronized void writePositionsInIndex(UniprotXmlIndex index,
			Map<String, Pair<Long, Long>> itemPositions, boolean appendOnIndexFile) throws IOException {
		// write the positions in the index
		if (!index.indexFile.getParentFile().exists()) {
			index.indexFile.getParentFile().mkdirs();
		}
		final FileOutputStream fos = new FileOutputStream(index.indexFile, appendOnIndexFile);
		FileLock lock = fos.getChannel().tryLock();
		while (lock == null) {
			lock = fos.getChannel().tryLock();
			try {
				Thread.sleep(1000);
			} catch (final InterruptedException e) {
			}
			log.info("Waiting for writting access to file " + index.indexFile.getAbsolutePath());
		}
		final BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(fos));

		final StringBuilder sb = new StringBuilder();
		try {
			for (final String key : itemPositions.keySet()) {
				final Pair<Long, Long> pair = itemPositions.get(key);
				sb.append(key + TAB + pair.getFirstelement() + TAB + pair.getSecondElement() + NEWLINE);
				log.debug("Writing in index: " + key + " positions:[" + pair.getFirstelement() + ","
						+ pair.getSecondElement() + "]");
			}
		} finally {
			bw.write(sb.toString());
			if (lock != null) {
				lock.release();
			}
			bw.close();
			index.status = Status.READY;
			// if (index.indexFile.length() % 1000 == 0){
			log.debug("Indexing done. Size of index: " + index.indexFile.length() / 1000 + " Kb");

			// }
		}

	}

	@Override
	public Entry getItem(String key) {
		try {
			// load index file
			loadIndexFile();
			// look for the provided key
			if (indexMap.containsKey(key)) {
				final Pair<Long, Long> pair = indexMap.get(key);
				final String item = uniprotFileIndexIO.getItem(pair.getFirstelement(), pair.getSecondElement());

				final Entry entry = uniprotFileIndexIO.unmarshallSingleEntryFromString(item);
				// in order to save memory, remove non used elements such as
				// references
				UniprotEntryUtil.removeNonUsedElements(entry, ignoreReferences, ignoreDBReferences);
				return entry;
			}
		} catch (final IndexException e) {
			e.printStackTrace();
			log.error(e.getMessage());
		} catch (final IOException e) {
			e.printStackTrace();
			log.error(e.getMessage());
		}
		log.debug("Item with key '" + key + "' is not in the index");
		return null;
	}

	// @Override
	// public List<Entry> getItems(Collection<String> keys) {
	// try {
	// // load index file
	// loadIndexFile();
	// final List<Entry> ret = new ArrayList<Entry>();
	// for (final String key : keys) {
	//
	// // look for the provided key
	// if (indexMap.containsKey(key)) {
	// final Pair<Long, Long> pair = indexMap.get(key);
	// final String item = uniprotFileIndexIO.getItem(pair.getFirstelement(),
	// pair.getSecondElement());
	//
	// final List<Entry> entries =
	// uniprotFileIndexIO.unmarshallMultipleEntriesFromString(item);
	// for (final Entry entry : entries) {
	// // in order to save memory, remove non used elements
	// // such as
	// // references
	// UniprotEntryUtil.removeNonUsedElements(entry, ignoreReferences,
	// ignoreDBReferences);
	// ret.add(entry);
	// }
	//
	// } else {
	// log.debug("Item with key '" + key + "' is not in the index");
	// }
	// }
	// if (!ret.isEmpty()) {
	// return ret;
	// }
	// } catch (final IndexException e) {
	// e.printStackTrace();
	// log.error(e.getMessage());
	// } catch (final IOException e) {
	// e.printStackTrace();
	// log.error(e.getMessage());
	// }
	//
	// return null;
	// }

	@Override
	public List<Entry> getItems(Collection<String> keys) {
		try {
			// load index file
			loadIndexFile();
			final List<Pair<Long, Long>> listofpairs = new ArrayList<Pair<Long, Long>>();
			for (final String key : keys) {
				if (indexMap.containsKey(key)) {
					final Pair<Long, Long> pair = indexMap.get(key);
					listofpairs.add(pair);
				}
			}
			// Merge consecutive pairs
			final List<Pair<Long, Long>> listofpairs2 = mergeConsecutivePairs(listofpairs, MAX_NUMBER_ENTRIES_PER_PAIR);

			final List<Entry> ret = new ArrayList<Entry>();
			for (final Pair<Long, Long> pair : listofpairs2) {

				final String item = uniprotFileIndexIO.getItem(pair.getFirstelement(), pair.getSecondElement());

				final List<Entry> entries = uniprotFileIndexIO.unmarshallMultipleEntriesFromString(item);
				for (final Entry entry : entries) {
					// in order to save memory, remove non used elements
					// such as
					// references
					UniprotEntryUtil.removeNonUsedElements(entry, ignoreReferences, ignoreDBReferences);
					ret.add(entry);
				}

			}
			if (!ret.isEmpty()) {
				return ret;
			}
		} catch (final IndexException e) {
			e.printStackTrace();
			log.error(e.getMessage());
		} catch (final IOException e) {
			e.printStackTrace();
			log.error(e.getMessage());
		}

		return null;
	}

	public Iterator<Entry> getIteratorOfItems(Collection<String> keys) {
		try {
			// load index file
			loadIndexFile();
			final List<Pair<Long, Long>> listofpairs = new ArrayList<Pair<Long, Long>>();
			for (final String key : keys) {
				if (indexMap.containsKey(key)) {
					final Pair<Long, Long> pair = indexMap.get(key);
					listofpairs.add(pair);
				}
			}
			// Merge consecutive pairs
			final List<Pair<Long, Long>> listofpairs2 = mergeConsecutivePairs(listofpairs, MAX_NUMBER_ENTRIES_PER_PAIR);
			return new UniprotXmlIndexIterator(listofpairs2, uniprotFileIndexIO, ignoreReferences, ignoreDBReferences);

		} catch (final IndexException e) {
			e.printStackTrace();
			log.error(e.getMessage());
		} catch (final IOException e) {
			e.printStackTrace();
			log.error(e.getMessage());
		}

		return null;
	}

	private List<Pair<Long, Long>> mergeConsecutivePairs(List<Pair<Long, Long>> listofpairs,
			int maxNumberOfEntriesPerPair) {

		// sort the pairs
		Collections.sort(listofpairs, new Comparator<Pair<Long, Long>>() {

			@Override
			public int compare(Pair<Long, Long> pair1, Pair<Long, Long> pair2) {
				final int compareTo = pair1.getFirstelement().compareTo(pair2.getFirstelement());
				if (compareTo != 0) {
					return compareTo;
				}
				return pair1.getSecondElement().compareTo(pair2.getSecondElement());
			}
		});
		final List<Pair<Long, Long>> ret = new ArrayList<Pair<Long, Long>>();
		// iterate over them and merge them if they are consecutive
		// we consider consecutive pairs when the second element of the first
		// pair is equals to the first element of the second pair
		for (int i = 0; i < listofpairs.size(); i++) {
			final Pair<Long, Long> pair1 = listofpairs.get(i);
			final long firstElement = pair1.getFirstelement();
			long secondElement = pair1.getSecondElement();
			long lastSecondElement = secondElement;
			int j = 1;
			while (j <= maxNumberOfEntriesPerPair && i + j < listofpairs.size()
					&& (pair1.equals(listofpairs.get(i + j))
							|| listofpairs.get(i + j).getFirstelement() == secondElement
							|| listofpairs.get(i + j).getFirstelement() == lastSecondElement)) {
				lastSecondElement = secondElement;
				secondElement = listofpairs.get(i + j).getSecondElement();
				j++;
			}
			if (j > 1) {
				final Pair<Long, Long> pair3 = new Pair<Long, Long>(firstElement, secondElement);
				ret.add(pair3);
				i += j - 1;
			} else {
				ret.add(pair1);

			}
		}
		if (ret.size() != listofpairs.size()) {
			log.debug("keys to access the index were merged to optimze access from " + listofpairs.size() + " to "
					+ ret.size());
		}
		return ret;
	}

	private Pair<Long, Long> mergePairs(Pair<Long, Long> pair1, Pair<Long, Long> pair2) {
		return new Pair<Long, Long>(pair1.getFirstelement(), pair2.getSecondElement());
	}

	protected void loadIndexFile() throws IOException {
		// if not ready, means that the index file has to be updated
		if (status == Status.NOT_READY) {
			indexMap.clear();
			if (indexFile == null || !indexFile.exists() || indexFile.length() <= 0)
				indexFile();
		}
		// if index Map is empty, read the index file
		if (indexMap.isEmpty()) {
			if (indexFile.length() > 0) {
				final FileInputStream fis = new FileInputStream(indexFile);
				FileLock lock = fis.getChannel().tryLock(0, Long.MAX_VALUE, true);
				while (lock == null) {
					lock = fis.getChannel().tryLock();
					try {
						Thread.sleep(1000);
					} catch (final InterruptedException e) {
					}
					log.info("Waiting for reading access to file " + indexFile.getAbsolutePath());
				}
				final BufferedReader fr = new BufferedReader(new InputStreamReader(fis));
				try {
					String line;
					while ((line = fr.readLine()) != null) {
						if (Thread.currentThread().isInterrupted()) {
							throw new RuntimeException("Thread interrupted");
						}
						final String[] split = line.split(TAB);
						final String key = split[0];
						final long start = new Long(split[1]);
						final long end = new Long(split[2]);
						final Pair<Long, Long> pair = new Pair<>(start, end);
						indexMap.put(key, pair);
					}
				} finally {
					if (!indexMap.isEmpty()) {
						log.info(indexMap.size() + " entries in the index");
					}
					if (lock != null) {
						lock.release();
					}
					fr.close();
					status = Status.READY;

				}
			}
		}
	}

	/**
	 * Adds an item to the index. It will be written in the indexed file, and the
	 * index file will be updated.
	 *
	 * @param item
	 * @return
	 * @throws IOException
	 */
	@Override
	public Map<String, Pair<Long, Long>> addItem(Entry entry, Set<String> keys) {
		Map<String, Pair<Long, Long>> itemPositions = null;
		// load index file
		try {
			loadIndexFile();

			// look if it is already in the index
			if (keys == null || keys.isEmpty()) {
				keys = uniprotFileIndexIO.getKeys(entry);
			}
			final Map<String, Pair<Long, Long>> ret = new THashMap<>();
			if (keys != null && !keys.isEmpty()) {
				for (final String key : keys) {
					if (indexMap.containsKey(key)) {
						final Pair<Long, Long> pair = indexMap.get(key);

						ret.put(key, pair);

					}
				}
			}
			if (!ret.isEmpty() && ret.size() == keys.size()) {
				return ret;
			}
			// add into the file to index
			final String item = marshallEntryToText(entry);
			itemPositions = uniprotFileIndexIO.addNewItem(item, keys);

			// add to the map
			indexMap.putAll(itemPositions);

			// write the index file appending
			writePositionsInIndex(this, itemPositions, true);

			// return the positions
			return itemPositions;
		} catch (final IOException e) {
			e.printStackTrace();
			log.error(e.getMessage());
		}
		return null;
	}

	private String marshallEntryToText(Entry entry) {
		final OutputStream output = new OutputStream() {
			private final StringBuilder string = new StringBuilder();

			@Override
			public void write(int b) throws IOException {
				string.append((char) b);
			}

			// Netbeans IDE automatically overrides this toString()
			@Override
			public String toString() {
				return string.toString();
			}
		};

		JAXB.marshal(entry, output);
		String string = output.toString();
		final int indexOf = string.indexOf("\n");
		if (indexOf > -1) {
			string = string.substring(indexOf + 1);
		}
		return string.trim();
	}

	public File getIndexedFile() {
		return fileToIndex;
	}

	public File getIndexFile() {
		return indexFile;
	}

	@Override
	public boolean isEmpty() {
		if (status == Status.NOT_READY) {
			try {
				loadIndexFile();
			} catch (final IOException e) {
				e.printStackTrace();
			} catch (final Exception e) {
				e.printStackTrace();
			}
		}
		if (indexMap.isEmpty()) {
			return true;
		}
		return false;
	}

	public Map<String, Pair<Long, Long>> addItems(List<Entry> entries) {
		final Map<String, Pair<Long, Long>> itemPositions = new THashMap<String, Pair<Long, Long>>();
		// load index file
		try {
			loadIndexFile();
			final Map<String, Set<String>> notFoundEntries = new THashMap<String, Set<String>>();
			for (final Entry entry : entries) {

				final Set<String> keys = uniprotFileIndexIO.getKeys(entry);

				if (keys != null && !keys.isEmpty()) {
					boolean found = true;
					for (final String key : keys) {
						if (indexMap.containsKey(key)) {
							final Pair<Long, Long> pair = indexMap.get(key);
							itemPositions.put(key, pair);
						} else {
							found = false;
						}
					}
					if (!found) {
						final String item = marshallEntryToText(entry);
						notFoundEntries.put(item, keys);
					}
				}

			}
			if (notFoundEntries.isEmpty()) {
				return itemPositions;
			}
			// write the rest

			// add into the file to index
			final Map<String, Pair<Long, Long>> positions = uniprotFileIndexIO.addNewItems(notFoundEntries);

			// add to the map
			indexMap.putAll(positions);

			// write the index file appending
			writePositionsInIndex(this, positions, true);

			// add to the returning map
			itemPositions.putAll(positions);
			// return the positions
			return itemPositions;
		} catch (

		final IOException e) {
			e.printStackTrace();
			log.error(e.getMessage());
		}
		return null;
	}

}
