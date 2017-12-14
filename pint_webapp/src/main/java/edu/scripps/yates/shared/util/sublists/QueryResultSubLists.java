package edu.scripps.yates.shared.util.sublists;

import java.io.Serializable;
import java.util.List;

public class QueryResultSubLists implements Serializable {
	/**
	 *
	 */
	private static final long serialVersionUID = 3500609597581892746L;
	private PsmBeanSubList psmSubList;
	private PeptideBeanSubList peptideSubList;
	private ProteinBeanSubList proteinSubList;
	private ProteinGroupBeanSubList proteinGroupSubList;
	private int numDifferentSequences;
	private int numDifferentSequencesDistinguishingModifieds;
	private List<String> proteinScores;
	private List<String> peptideScores;
	private List<String> psmScores;
	private List<String> ptmScores;
	private List<String> scoreTypes;

	public QueryResultSubLists(List<String> proteinScores, List<String> peptideScores, List<String> psmScores,
			List<String> ptmScores, List<String> scoreTypes) {
		// just grab the score names
		setProteinScores(proteinScores);
		setPeptideScores(peptideScores);
		setPsmScores(psmScores);
		setPtmScores(ptmScores);
		setScoreTypes(scoreTypes);
	}

	public QueryResultSubLists() {

	}

	/**
	 * @return the psmSubList
	 */
	public PsmBeanSubList getPsmSubList() {
		return psmSubList;
	}

	/**
	 * @param psmSubList
	 *            the psmSubList to set
	 */
	public void setPsmSubList(PsmBeanSubList psmSubList) {
		this.psmSubList = psmSubList;
	}

	/**
	 * @return the proteinSubList
	 */
	public ProteinBeanSubList getProteinSubList() {
		return proteinSubList;
	}

	/**
	 * @param proteinSubList
	 *            the proteinSubList to set
	 */
	public void setProteinSubList(ProteinBeanSubList proteinSubList) {
		this.proteinSubList = proteinSubList;
	}

	/**
	 * @return the proteinGroupSubList
	 */
	public ProteinGroupBeanSubList getProteinGroupSubList() {
		return proteinGroupSubList;
	}

	/**
	 * @param proteinGroupSubList
	 *            the proteinGroupSubList to set
	 */
	public void setProteinGroupSubList(ProteinGroupBeanSubList proteinGroupSubList) {
		this.proteinGroupSubList = proteinGroupSubList;
	}

	public boolean isEmpty() {
		if (proteinSubList != null)
			return proteinSubList.isEmpty();
		return true;

	}

	/**
	 * @return the numDifferentSequences
	 */
	public int getNumDifferentSequences() {
		return numDifferentSequences;
	}

	/**
	 * @param numDifferentSequences
	 *            the numDifferentSequences to set
	 */
	public void setNumDifferentSequences(int numDifferentSequences) {
		this.numDifferentSequences = numDifferentSequences;
	}

	/**
	 * @return the numDifferentSequencesDistinguishingModifieds
	 */
	public int getNumDifferentSequencesDistinguishingModifieds() {
		return numDifferentSequencesDistinguishingModifieds;
	}

	/**
	 * @param numDifferentSequencesDistinguishingModifieds
	 *            the numDifferentSequencesDistinguishingModifieds to set
	 */
	public void setNumDifferentSequencesDistinguishingModifieds(int numDifferentSequencesDistinguishingModifieds) {
		this.numDifferentSequencesDistinguishingModifieds = numDifferentSequencesDistinguishingModifieds;
	}

	/**
	 * @return the peptideSubList
	 */
	public PeptideBeanSubList getPeptideSubList() {
		return peptideSubList;
	}

	/**
	 * @param peptideSubList
	 *            the peptideSubList to set
	 */
	public void setPeptideSubList(PeptideBeanSubList peptideSubList) {
		this.peptideSubList = peptideSubList;
	}

	public List<String> getProteinScores() {
		return proteinScores;
	}

	public void setProteinScores(List<String> proteinScores) {
		this.proteinScores = proteinScores;
	}

	public List<String> getPeptideScores() {
		return peptideScores;
	}

	public void setPeptideScores(List<String> peptideScores) {
		this.peptideScores = peptideScores;
	}

	public List<String> getPsmScores() {
		return psmScores;
	}

	public void setPsmScores(List<String> psmScores) {
		this.psmScores = psmScores;
	}

	public List<String> getPtmScores() {
		return ptmScores;
	}

	public void setPtmScores(List<String> ptmScores) {
		this.ptmScores = ptmScores;
	}

	public List<String> getScoreTypes() {
		return scoreTypes;
	}

	public void setScoreTypes(List<String> scoreTypes) {
		this.scoreTypes = scoreTypes;
	}
}
