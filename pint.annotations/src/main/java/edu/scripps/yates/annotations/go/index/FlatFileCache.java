package edu.scripps.yates.annotations.go.index;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;

import edu.scripps.yates.annotations.go.GORetriever;
import edu.scripps.yates.annotations.go.GoEntry;

public class FlatFileCache {
	private final File indexFile;
	private final static Map<String, Set<GoEntry>> entriesByID = new HashMap<String, Set<GoEntry>>();
	private boolean ready = false;
	private final static Logger log = Logger.getLogger(FlatFileCache.class);

	public FlatFileCache(String path) {
		indexFile = new File(path);

	}

	private void setReady() {
		if (!ready) {
			readIndex();
			ready = true;
		}
	}

	public synchronized Set<GoEntry> getEntriesByID(String id) {
		setReady();
		if (entriesByID.containsKey(id)) {
			return entriesByID.get(id);
		}
		return Collections.emptySet();
	}

	public synchronized Map<String, Set<GoEntry>> getEntriesByID(Collection<String> ids) {
		Map<String, Set<GoEntry>> map = new HashMap<String, Set<GoEntry>>();
		setReady();
		for (String id : ids) {
			if (entriesByID.containsKey(id)) {
				map.put(id, entriesByID.get(id));
			}
		}
		return map;
	}

	public synchronized void addToIndex(Collection<GoEntry> entries) {
		setReady();
		Set<GoEntry> notInIndex = getNotInIndex(entries);
		if (!notInIndex.isEmpty()) {
			PrintWriter out = null;
			try {
				FileWriter fw = new FileWriter(indexFile, true);

				BufferedWriter bw = new BufferedWriter(fw);
				out = new PrintWriter(bw);
				for (GoEntry goEntry : notInIndex) {
					out.println(goEntry.toLine());
					addToMap(entriesByID, goEntry);
				}
				log.debug(notInIndex.size() + " entries written in index");
				log.debug("Index with " + entriesByID.size() + " entries");
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				out.close();
			}

		}

	}

	private Set<GoEntry> getNotInIndex(Collection<GoEntry> entries) {
		Set<GoEntry> notInIndex = new HashSet<GoEntry>();
		for (GoEntry goEntry : entries) {
			if (!entriesByID.containsKey(goEntry.getId())) {
				notInIndex.add(goEntry);
			}
		}
		return notInIndex;
	}

	private Map<String, Set<GoEntry>> readIndex() {
		log.info("Reading GO term index...");
		if (indexFile.exists()) {
			BufferedReader rd = null;
			try {
				rd = new BufferedReader(new FileReader(indexFile));
				String line = null;
				boolean firsLine = true;
				while ((line = rd.readLine()) != null) {
					if (firsLine) {
						final String[] split = line.split("\t");
						int index = 0;
						for (String string : split) {
							GORetriever.indexByColumnName.put(string, index++);
						}
						firsLine = false;
					} else {
						GoEntry goEntry = new GoEntry(line);
						addToMap(entriesByID, goEntry);
					}
				}
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				try {
					rd.close();
				} catch (IOException e) {

				}
			}
		} else {
			writeHeader();
		}
		log.info("Readed GO terms for " + entriesByID.size() + " entries");
		return entriesByID;
	}

	private void writeHeader() {
		PrintWriter out = null;
		try {
			FileWriter fw = new FileWriter(indexFile, true);

			BufferedWriter bw = new BufferedWriter(fw);
			out = new PrintWriter(bw);
			int index = 0;
			;
			out.write("DB");

			GORetriever.indexByColumnName.put("DB", index++);
			out.write("\t");
			out.write("ID");
			GORetriever.indexByColumnName.put("ID", index++);
			out.write("\t");
			out.write("Splice");
			GORetriever.indexByColumnName.put("Splice", index++);
			out.write("\t");
			out.write("Symbol");
			GORetriever.indexByColumnName.put("Symbol", index++);
			out.write("\t");
			out.write("Taxon");
			GORetriever.indexByColumnName.put("Taxon", index++);
			out.write("\t");
			out.write("Qualifier");
			GORetriever.indexByColumnName.put("Qualifier", index++);
			out.write("\t");
			out.write("GO ID");
			GORetriever.indexByColumnName.put("GO ID", index++);
			out.write("\t");
			out.write("GO Name");
			GORetriever.indexByColumnName.put("GO Name", index++);
			out.write("\t");
			out.write("Reference");
			GORetriever.indexByColumnName.put("Reference", index++);
			out.write("\t");
			out.write("Evidence");
			GORetriever.indexByColumnName.put("Evidence", index++);
			out.write("\t");
			out.write("With");
			GORetriever.indexByColumnName.put("With", index++);
			out.write("\t");
			out.write("Aspect");
			GORetriever.indexByColumnName.put("Aspect", index++);
			out.write("\t");
			out.write("Date");
			GORetriever.indexByColumnName.put("Date", index++);
			out.write("\t");
			out.write("Source");
			GORetriever.indexByColumnName.put("Source", index++);
			out.write("\n");
			log.info("header written");
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			out.close();
		}

	}

	private void addToMap(Map<String, Set<GoEntry>> ret, GoEntry goEntry) {
		if (ret.containsKey(goEntry.getId())) {
			ret.get(goEntry.getId()).add(goEntry);
		} else {
			Set<GoEntry> set = new HashSet<GoEntry>();
			set.add(goEntry);
			ret.put(goEntry.getId(), set);
		}
	}
}
