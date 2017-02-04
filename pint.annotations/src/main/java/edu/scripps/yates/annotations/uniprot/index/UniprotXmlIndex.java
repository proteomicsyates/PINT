package edu.scripps.yates.annotations.uniprot.index;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.xml.bind.JAXB;
import javax.xml.bind.JAXBException;

import org.apache.commons.io.FilenameUtils;
import org.apache.log4j.Logger;

import edu.scripps.yates.annotations.uniprot.xml.Entry;
import edu.scripps.yates.utilities.dates.DatesUtil;
import edu.scripps.yates.utilities.index.FileIndex;
import edu.scripps.yates.utilities.index.TextFileIndex;
import edu.scripps.yates.utilities.util.Pair;

public class UniprotXmlIndex implements FileIndex<Entry> {
	private static final Logger log = Logger.getLogger(TextFileIndex.class);
	private final File fileToIndex;
	private final File indexFile;
	private final HashMap<String, Pair<Long, Long>> indexMap = new HashMap<String, Pair<Long, Long>>();
	private final static String INDEX_EXT = ".idx";
	private static final String TAB = "\t";
	private static final String NEWLINE = "\n";
	private final UniprotXmlIndexIO uniprotFileIndexIO;

	private enum Status {
		READY, NOT_READY
	};

	private Status status = Status.NOT_READY;

	public UniprotXmlIndex(File file) throws IOException, JAXBException {
		fileToIndex = file;
		// create the index file
		indexFile = new File(getIndexPathName(file));
		uniprotFileIndexIO = new UniprotXmlIndexIO(fileToIndex);

	}

	private String getIndexPathName(File file) {
		String pathName = file.getParent() + File.separator + FilenameUtils.getBaseName(file.getAbsolutePath())
				+ INDEX_EXT;
		return pathName;
	}

	public UniprotXmlIndex(String path) throws IOException, JAXBException {
		this(new File(path));
	}

	private void indexFile() throws IOException {
		log.info("Indexing file " + FilenameUtils.getName(fileToIndex.getAbsolutePath()) + "...");
		long t1 = System.currentTimeMillis();
		// read the index, getting the positions of the items
		final Map<String, Pair<Long, Long>> indexMap = uniprotFileIndexIO.getIndexMap();
		// add to the map
		this.indexMap.putAll(indexMap);
		long t2 = System.currentTimeMillis();
		// write the index file without appending
		writePositionsInIndex(this, indexMap, false);
		long t3 = System.currentTimeMillis();

		log.info(DatesUtil.getDescriptiveTimeFromMillisecs(t2 - t1) + " to read the input file");
		log.info(DatesUtil.getDescriptiveTimeFromMillisecs(t3 - t2) + " to write the index to "
				+ indexFile.getAbsolutePath());
	}

	private static synchronized void writePositionsInIndex(UniprotXmlIndex index,
			Map<String, Pair<Long, Long>> itemPositions, boolean appendOnIndexFile) throws IOException {
		// write the positions in the index
		FileWriter fw = new FileWriter(index.indexFile, appendOnIndexFile);
		try {
			for (String key : itemPositions.keySet()) {
				final Pair<Long, Long> pair = itemPositions.get(key);
				fw.write(key + TAB + pair.getFirstelement() + TAB + pair.getSecondElement() + NEWLINE);
				log.debug("Writing in index: " + key + " positions:[" + pair.getFirstelement() + ","
						+ pair.getSecondElement() + "]");
			}
		} finally {
			fw.close();
			index.status = Status.READY;
			if (index.indexFile.length() % 1000 == 0)
				log.info("Indexing done. Size of index: " + index.indexFile.length() / 1000 + " Kb");
		}

	}

	@Override
	public Entry getItem(String key) {
		try {
			// load index file
			final Map<String, Pair<Long, Long>> indexMap = loadIndexFile();
			// look for the provided key
			if (indexMap.containsKey(key)) {
				final Pair<Long, Long> pair = indexMap.get(key);
				String item = uniprotFileIndexIO.getItem(pair.getFirstelement(), pair.getSecondElement());

				Entry entry = uniprotFileIndexIO.unmarshallFromString(item);
				return entry;
			}
		} catch (IOException e) {
			e.printStackTrace();
			log.error(e.getMessage());
		}
		log.debug("Item with key '" + key + "' is not in the index");
		return null;
	}

	private Map<String, Pair<Long, Long>> loadIndexFile() throws IOException {
		// if not ready, means that the index file has to be updated
		if (status == Status.NOT_READY) {
			indexMap.clear();
			if (indexFile == null || !indexFile.exists() || indexFile.length() <= 0)
				indexFile();
		}
		// if index Map is empty, read the index file
		if (indexMap.isEmpty()) {
			BufferedReader fr = new BufferedReader(new InputStreamReader(new FileInputStream(indexFile)));
			try {
				String line;
				while ((line = fr.readLine()) != null) {
					final String[] split = line.split(TAB);
					String key = split[0];
					long start = new Long(split[1]);
					long end = new Long(split[2]);
					Pair<Long, Long> pair = new Pair<Long, Long>(start, end);
					indexMap.put(key, pair);
				}
			} finally {
				log.info(indexMap.size() + " entries in the index");
				fr.close();
				status = Status.READY;
			}
		}

		return indexMap;
	}

	/**
	 * Adds an item to the index. It will be written in the indexed file, and
	 * the index file will be updated.
	 *
	 * @param item
	 * @return
	 * @throws IOException
	 */
	@Override
	public Map<String, Pair<Long, Long>> addItem(Entry entry) {

		// load index file
		try {
			final Map<String, Pair<Long, Long>> indexMap = loadIndexFile();

			String item = marshallEntryToText(entry);

			// look if it is already in the index
			final Set<String> keys = uniprotFileIndexIO.getKeys(item);
			Map<String, Pair<Long, Long>> ret = new HashMap<String, Pair<Long, Long>>();
			if (keys != null && !keys.isEmpty()) {
				for (String key : keys) {
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
			final Map<String, Pair<Long, Long>> itemPositions = uniprotFileIndexIO.addNewItem(item);

			// add to the map
			indexMap.putAll(itemPositions);

			// write the index file appending
			writePositionsInIndex(this, itemPositions, true);

			// return the positions
			return itemPositions;
		} catch (IOException e) {
			e.printStackTrace();
			log.error(e.getMessage());
		}
		return null;
	}

	private String marshallEntryToText(Entry entry) {
		OutputStream output = new OutputStream() {
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
		if (indexOf > -1)
			string = string.substring(indexOf + 1);

		return string.trim();
	}

	public File getIndexedFile() {
		return fileToIndex;
	}

	public File getIndexFile() {
		return indexFile;
	}

}
