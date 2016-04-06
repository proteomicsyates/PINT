package edu.scripps.yates.dbindex;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

/**
 * Protein sequence cache
 * 
 * Supports addition of proteins and constant access of protein sequence by id
 * Abstracts out the underlying storage.
 * 
 * Singleton, so we do not waste memory in multi-threaded setup Thread-safe, can
 * be shared between multiple reader threads.
 * 
 * @author Adam
 */
public class ProteinCache {
	private static final Logger log = Logger.getLogger(ProteinCache.class);
	private final ArrayList<String> sequences;
	private final ArrayList<String> defs;
	public static final int EST_SIZE = 200000;
	private static ProteinCache instance = null;

	// /////////////////
	// Salva 15 Apr 2015
	// Disabled because it cause problems to use the same protein cache for
	// multiple indexes
	// /////////////////
	// public synchronized static ProteinCache getInstance() {
	// if (instance == null) {
	// instance = new ProteinCache();
	// }
	// return instance;
	// }
	// /////////////////

	/**
	 * Create a new empty cache
	 */
	public ProteinCache() {
		sequences = new ArrayList<String>(EST_SIZE);
		defs = new ArrayList<String>(EST_SIZE);
	}

	public int getNumberProteins() {
		return sequences.size();
	}

	/**
	 * get protein by id, protein id start at 1
	 * 
	 * @param proteinId
	 *            protein id, 1 based
	 * @return protein sequence string
	 */
	String getProteinSequence(long proteinId) {
		return sequences.get((int) proteinId);
	}

	/**
	 * get protein by id, protein id start at 1
	 * 
	 * @param proteinId
	 *            protein id, 1 based
	 * @return protein sequence string
	 */
	String getProteinDef(long proteinId) {
		return defs.get((int) proteinId);
	}

	/**
	 * add protein to the cache Assumes the protein is added in the protein
	 * id-ordering
	 * 
	 * @param def
	 *            prot. def to add
	 * @param protein
	 *            sequence of protein to add to the cache
	 */
	void addProtein(String def, String protein) {
		defs.add(def);
		sequences.add(protein);
	}

	/*
	 * @harshil Shah
	 */
	public void addProtein(String def) {
		defs.add(def);

	}

	/**
	 * Get peptide sequence as protein subsequence, does not check bounds
	 * 
	 * @param protId
	 * @param seqOffset
	 * @param seqLen
	 * @return
	 */
	String getPeptideSequence(long protId, int seqOffset, int seqLen) {
		String protSeq = getProteinSequence(protId);
		String peptideSeq = null;

		try {

			peptideSeq = protSeq.substring(seqOffset, seqOffset + seqLen);
		} catch (Exception e) {
			e.printStackTrace();
			log.error("Error tryin to get substring from " + protSeq);
			log.error("Using beginIndex:" + seqOffset + " and length: "
					+ seqLen);
			log.error(e.getMessage());
		}

		return peptideSeq;
	}

	/**
	 * clear the protein cache
	 */
	void clear() {
		sequences.clear();
		defs.clear();
	}

	/**
	 * check if the cache has been populated
	 * 
	 * @return true if populated / not empty
	 */
	synchronized boolean isPopulated() {
		return !sequences.isEmpty();
	}

	public List<IndexedProtein> getProteins(IndexedSequence sequence) {
		List<IndexedProtein> ret = new ArrayList<IndexedProtein>();

		for (int proteinId : sequence.getProteinIds()) {
			IndexedProtein ip = new IndexedProtein(getProteinDef(proteinId),
					proteinId);
			ret.add(ip);
		}
		return ret;
	}
}
