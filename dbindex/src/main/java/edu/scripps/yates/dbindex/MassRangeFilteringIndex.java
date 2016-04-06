package edu.scripps.yates.dbindex;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 *
 * Special temporary index store that is used to store in memory sequences as
 * they are being cut that fit in mass ranges. After all have been cut, the
 * fitting sequences can get returned.
 *
 * Life-cycle of this object should be during a single indexer.getSequences()
 * invocation
 *
 * @author Adam
 */
public class MassRangeFilteringIndex implements DBIndexStore {

	private double[] minMasses;
	private double[] maxMasses;
	private int numRanges;
	private ProteinCache proteinCache;
	// use set to remove dups
	private final Map<String, IndexedSequence> matchingSequences = new HashMap<String, IndexedSequence>();

	MassRangeFilteringIndex() {
	}

	void init(List<MassRange> massRanges) {

		// reset from previous
		matchingSequences.clear();

		// initialize for new search and reset
		numRanges = massRanges.size();

		minMasses = new double[numRanges];
		maxMasses = new double[numRanges];

		int rangeNum = 0;
		for (MassRange range : massRanges) {
			final double precMass = range.getPrecMass();
			final double tol = range.getTolerance();

			double minMass = precMass - tol;
			if (minMass == 0) {
				minMass = 0f;
			}
			double maxMass = precMass + tol;
			minMasses[rangeNum] = minMass;
			maxMasses[rangeNum] = maxMass;

			++rangeNum;
		}

	}

	@Override
	public void init(String databaseID) throws DBIndexStoreException {
	}

	@Override
	public void startAddSeq() throws DBIndexStoreException {
		// nothing to do
	}

	@Override
	public void stopAddSeq() throws DBIndexStoreException {
		// nothing to do
	}

	@Override
	public boolean indexExists() throws DBIndexStoreException {
		return false;

	}

	@Override
	public FilterResult filterSequence(double precMass, String sequence) {
		// check range masses
		boolean skipStart = true;
		for (int range = 0; range < numRanges; ++range) {
			if (precMass <= maxMasses[range]) {
				skipStart = false;
				if (precMass >= minMasses[range]) {
					return FilterResult.INCLUDE;
				}
			}

		}

		if (skipStart) {
			return FilterResult.SKIP_PROTEIN_START;
		} else {
			return FilterResult.SKIP;
		}
	}

	@Override
	public void addSequence(double precMass, int sequenceOffset, int sequenceLen, String sequence, String resLeft,
			String resRight, long proteinId) throws DBIndexStoreException {

		// it already passed the filter, so just add the sequence
		// or if exists, add the protein
		IndexedSequence indexedSequence = matchingSequences.get(sequence);

		if (indexedSequence == null) {
			indexedSequence = new IndexedSequence(precMass, sequenceOffset, sequenceLen, sequence, resLeft, resRight,
					proteinId);
			matchingSequences.put(sequence, indexedSequence);
		} else {
			// add protein to existing
			List<Integer> protIds = indexedSequence.getProteinIds();
			if (!protIds.contains((int) proteinId)) {
				protIds.add((int) proteinId);
			}
		}

	}

	@Override
	public List<IndexedSequence> getSequences(double precMass, double tolerance) throws DBIndexStoreException {
		// return all matched sequences
		return new ArrayList<IndexedSequence>(matchingSequences.values());
	}

	@Override
	public List<IndexedSequence> getSequences(List<MassRange> ranges) throws DBIndexStoreException {
		// return all matched sequences
		return new ArrayList<IndexedSequence>(matchingSequences.values());
	}

	@Override
	public long addProteinDef(long num, String accession, String protSequence) throws DBIndexStoreException {
		// nothing to do, seq-protein associations are stored in indexed
		// sequence itself
		return num;
	}

	@Override
	public void setProteinCache(ProteinCache proteinCache) {
		this.proteinCache = proteinCache;
	}

	@Override
	public boolean supportsProteinCache() {
		return true;
	}

	@Override
	public List<IndexedProtein> getProteins(IndexedSequence sequence) throws DBIndexStoreException {
		List<Integer> proteinIds = sequence.getProteinIds();

		List<IndexedProtein> ret = new ArrayList<IndexedProtein>();

		for (int protId : proteinIds) {
			IndexedProtein indexedProtein = new IndexedProtein(protId);

			final String accession = proteinCache.getProteinDef(protId);
			indexedProtein.setAccession(accession);

			ret.add(indexedProtein);

		}

		return ret;
	}

	@Override
	public long getNumberSequences() throws DBIndexStoreException {
		return matchingSequences.size();
	}

	@Override
	public ResidueInfo getResidues(IndexedSequence peptideSequence, IndexedProtein protein)
			throws DBIndexStoreException {
		// stored in the sequence itself, no need to calculate
		ResidueInfo residues = new ResidueInfo(peptideSequence.getResLeft(), peptideSequence.getResRight());
		return residues;
	}

	@Override
	public void lastBuffertoDatabase() {
		throw new UnsupportedOperationException("Not supported yet."); // To
																		// change
																		// body
																		// of
																		// generated
																		// methods,
																		// choose
																		// Tools
																		// |
																		// Templates.
	}

	@Override
	public Iterator<IndexedSequence> getSequencesIterator(List<MassRange> ranges) throws DBIndexStoreException {
		return getSequences(ranges).iterator();
	}
}
