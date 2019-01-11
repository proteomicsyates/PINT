package edu.scripps.yates.shared.util;

import java.io.Serializable;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import edu.scripps.yates.shared.model.ProteinPeptideCluster;

public class ProteinPeptideClusterAlignmentResults implements Serializable {

	/**
	 *
	 */
	private static final long serialVersionUID = 1851608856670492776L;
	private ProteinPeptideCluster cluster;
	private Map<String, Set<AlignmentResult>> resultsByPeptideSequence = new HashMap<String, Set<AlignmentResult>>();

	public ProteinPeptideClusterAlignmentResults() {

	}

	public ProteinPeptideClusterAlignmentResults(ProteinPeptideCluster cluster) {
		setCluster(cluster);
	}

	public void setResultsByPeptideSequence(Map<String, Set<AlignmentResult>> resultsByPeptideSequence) {
		this.resultsByPeptideSequence = resultsByPeptideSequence;
	}

	public void addAlignmentResult(String peptide1, String peptide2, AlignmentResult alignmentResult) {
		addToMap(peptide1, alignmentResult);
		addToMap(peptide2, alignmentResult);
	}

	private void addToMap(String key, AlignmentResult result) {
		if (resultsByPeptideSequence.containsKey(key)) {
			resultsByPeptideSequence.get(key).add(result);
		} else {
			Set<AlignmentResult> set = new HashSet<AlignmentResult>();
			set.add(result);
			resultsByPeptideSequence.put(key, set);
		}
	}

	public Set<AlignmentResult> getAlignmentResults(String peptide1, String peptide2) {

		if (resultsByPeptideSequence.containsKey(peptide1) && resultsByPeptideSequence.containsKey(peptide2)) {
			Set<AlignmentResult> ret = new HashSet<AlignmentResult>();
			for (AlignmentResult alignmentResult : resultsByPeptideSequence.get(peptide1)) {
				if (alignmentResult.getSeq1().getSequence().equals(peptide1)
						&& alignmentResult.getSeq2().getSequence().equals(peptide2)) {
					ret.add(alignmentResult);
				} else if (alignmentResult.getSeq1().getSequence().equals(peptide2)
						&& alignmentResult.getSeq2().getSequence().equals(peptide1)) {
					ret.add(alignmentResult);
				}
			}
		}
		return null;
	}

	/**
	 * @return the cluster
	 */
	public ProteinPeptideCluster getCluster() {
		return cluster;
	}

	/**
	 * @param cluster
	 *            the cluster to set
	 */
	public void setCluster(ProteinPeptideCluster cluster) {
		this.cluster = cluster;
	}

	/**
	 * @return the resultsByKey
	 */
	public Map<String, Set<AlignmentResult>> getResultsByPeptideSequence() {
		return resultsByPeptideSequence;
	}

	/**
	 * @param resultsByPeptideSequence
	 *            the resultsByKey to set
	 */
	public void setResultsByKey(Map<String, Set<AlignmentResult>> resultsByPeptideSequence) {
		this.resultsByPeptideSequence = resultsByPeptideSequence;
	}

}
