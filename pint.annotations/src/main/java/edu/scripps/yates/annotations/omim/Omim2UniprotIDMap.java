package edu.scripps.yates.annotations.omim;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.springframework.core.io.ClassPathResource;

public class Omim2UniprotIDMap {
	private static final Logger log = Logger.getLogger(Omim2UniprotIDMap.class);
	private boolean loaded = false;
	private final Map<String, List<Integer>> uniprotToOmim = new HashMap<String, List<Integer>>();
	private final Map<Integer, List<String>> omimToUniprot = new HashMap<Integer, List<String>>();
	private String version;

	public List<Integer> uniprot2Omim(String uniprotAcc) {
		if (!loaded) {
			load();
		}
		return uniprotToOmim.get(uniprotAcc);
	}

	public Map<String, Set<Integer>> uniprot2Omim(Collection<String> uniprotAccs) {
		Map<String, Set<Integer>> ret = new HashMap<String, Set<Integer>>();
		for (String uniprotAcc : uniprotAccs) {
			final List<Integer> uniprot2Omim = uniprot2Omim(uniprotAcc);
			if (uniprot2Omim != null) {
				if (ret.containsKey(uniprotAcc)) {
					ret.get(uniprotAcc).addAll(uniprot2Omim);
				} else {
					Set<Integer> set = new HashSet<Integer>();
					set.addAll(uniprot2Omim);
					ret.put(uniprotAcc, set);
				}
			}
		}
		return ret;
	}

	public List<String> omim2Uniprot(Integer omimID) {
		if (!loaded) {
			load();
		}
		return omimToUniprot.get(omimID);
	}

	public Map<Integer, Set<String>> omim2Uniprot(Collection<Integer> omimIDs) {
		Map<Integer, Set<String>> ret = new HashMap<Integer, Set<String>>();
		for (Integer omimID : omimIDs) {
			final List<String> uniprotAccs = omim2Uniprot(omimID);
			if (uniprotAccs != null) {
				if (ret.containsKey(omimID)) {
					ret.get(omimID).addAll(uniprotAccs);
				} else {
					Set<String> set = new HashSet<String>();
					set.addAll(uniprotAccs);
					ret.put(omimID, set);
				}
			}
		}
		return ret;
	}

	private void load() {
		log.info("Loading mapping file");
		BufferedReader br = null;

		try {

			String sCurrentLine;

			br = new BufferedReader(new InputStreamReader(new DataInputStream(
					new ClassPathResource("mimtosp.txt").getInputStream())));
			int numLine = 0;
			int omimID = 0;
			while ((sCurrentLine = br.readLine()) != null) {
				if (numLine == 0) {
					if (sCurrentLine.contains("Release")) {
						version = sCurrentLine.substring(sCurrentLine.indexOf(" ")).trim();
						version = version.substring(0, version.indexOf(" ")).trim();
					}
				} else {
					if (sCurrentLine.indexOf(":") >= 0) {
						omimID = Integer.valueOf(sCurrentLine.substring(0, sCurrentLine.indexOf(":")));
					}
					while (sCurrentLine.indexOf("(") >= 0) {
						String tmp = sCurrentLine.substring(sCurrentLine.indexOf("(") + 1);
						String acc = tmp.substring(0, tmp.indexOf(")"));
						addMapping(acc, omimID);
						sCurrentLine = tmp.substring(tmp.indexOf(")") + 1);
					}
				}
				numLine++;
			}
			log.info(omimToUniprot.size() + " OMIM entries and " + uniprotToOmim.size() + " Uniprot entries");
			loaded = true;
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (br != null)
					br.close();
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
	}

	private void addMapping(String acc, int omimID) {
		if (omimToUniprot.containsKey(omimID)) {
			omimToUniprot.get(omimID).add(acc);
		} else {
			List<String> list = new ArrayList<String>();
			list.add(acc);
			omimToUniprot.put(omimID, list);
		}
		if (uniprotToOmim.containsKey(acc)) {
			uniprotToOmim.get(acc).add(omimID);
		} else {
			List<Integer> list = new ArrayList<Integer>();
			list.add(omimID);
			uniprotToOmim.put(acc, list);
		}

	}

	/**
	 * @return the uniprotToOmim
	 */
	public Map<String, List<Integer>> getUniprotToOmim() {
		if (!loaded)
			load();
		return uniprotToOmim;
	}

	/**
	 * @return the omimToUniprot
	 */
	public Map<Integer, List<String>> getOmimToUniprot() {
		if (!loaded)
			load();
		return omimToUniprot;
	}
}
