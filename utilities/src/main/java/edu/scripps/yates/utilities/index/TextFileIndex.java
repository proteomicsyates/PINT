package edu.scripps.yates.utilities.index;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.io.FilenameUtils;
import org.apache.log4j.Logger;

import edu.scripps.yates.utilities.util.Pair;

public class TextFileIndex implements FileIndex<String> {
	private final Logger log = Logger.getLogger(TextFileIndex.class);
	private final File fileToIndex;
	private final File indexFile;
	private final HashMap<String, Pair<Long, Long>> indexMap = new HashMap<String, Pair<Long, Long>>();
	private final static String INDEX_EXT = ".idx";
	private static final String TAB = "\t";
	private static final String NEWLINE = "\n";
	private final TextFileIndexIO textFileIndexIO;

	private enum Status {
		READY, NOT_READY
	};

	private Status status = Status.NOT_READY;

	public TextFileIndex(File file, String beginToken, String endToken)
			throws IOException {
		fileToIndex = file;
		// create the index file
		indexFile = new File(getIndexPathName(file));
		textFileIndexIO = new TextFileIndexIO(fileToIndex, beginToken, endToken);
	}

	private String getIndexPathName(File file) {
		String pathName = file.getParent() + File.separator
				+ FilenameUtils.getBaseName(file.getAbsolutePath()) + INDEX_EXT;
		return pathName;
	}

	public TextFileIndex(String path, String beginToken, String endToken)
			throws IOException {
		this(new File(path), beginToken, endToken);
	}

	private void indexFile() throws IOException {
		log.info("Indexing file "
				+ FilenameUtils.getName(fileToIndex.getAbsolutePath()) + "...");
		// read the index, getting the positions of the items
		final Map<String, Pair<Long, Long>> indexMap = textFileIndexIO
				.getIndexMap();
		// add to the map
		this.indexMap.putAll(indexMap);
		// write the index file without appending
		writePositionsInIndex(indexMap, false);

	}

	private void writePositionsInIndex(
			Map<String, Pair<Long, Long>> itemPositions,
			boolean appendOnIndexFile) throws IOException {
		// write the positions in the index
		FileWriter fw = new FileWriter(indexFile, appendOnIndexFile);
		try {
			for (String key : itemPositions.keySet()) {
				final Pair<Long, Long> pair = itemPositions.get(key);
				fw.write(key + TAB + pair.getFirstelement() + TAB
						+ pair.getSecondElement() + NEWLINE);
			}
		} finally {
			fw.close();
			status = Status.READY;
			log.info("Indexing done. File of index: " + indexFile.length()
					+ " bytes");
		}

	}

	public String getItem(String key) {
		try {
			// load index file
			final Map<String, Pair<Long, Long>> indexMap = loadIndexFile();
			// look for the provided key
			if (indexMap.containsKey(key)) {
				final Pair<Long, Long> pair = indexMap.get(key);
				String item = textFileIndexIO.getItem(pair.getFirstelement(),
						pair.getSecondElement());
				return item;
			}
		} catch (IOException e) {
			e.printStackTrace();
			log.error(e.getMessage());
		}
		log.info("Item with key '" + key + "' is not in the index");
		return null;
	}

	private Map<String, Pair<Long, Long>> loadIndexFile() throws IOException {
		// if not ready, means that the index file has to be updated
		if (status == Status.NOT_READY) {
			indexMap.clear();
			indexFile();
		}
		// if index Map is empty, read the index file
		if (indexMap.isEmpty()) {
			BufferedReader fr = new BufferedReader(new InputStreamReader(
					new FileInputStream(indexFile)));
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
				fr.close();
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
	public Map<String, Pair<Long, Long>> addItem(String item) {
		// load index file
		try {
			loadIndexFile();

			// add into the file to index

			final Map<String, Pair<Long, Long>> itemPositions = textFileIndexIO
					.addNewItem(item);

			// add to the map
			indexMap.putAll(itemPositions);

			// write the index file appending
			writePositionsInIndex(itemPositions, true);

			// return the positions
			return itemPositions;
		} catch (IOException e) {
			e.printStackTrace();
			log.error(e.getMessage());
		}
		return null;
	}

}
