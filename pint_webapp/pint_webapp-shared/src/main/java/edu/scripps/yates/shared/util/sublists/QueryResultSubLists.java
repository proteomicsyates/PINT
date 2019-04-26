package edu.scripps.yates.shared.util.sublists;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import edu.scripps.yates.shared.model.RatioDescriptorBean;

public class QueryResultSubLists implements Serializable {
	/**
	 *
	 */
	private static final long serialVersionUID = 3500609597581892746L;
//	private PsmBeanSubList psmSubList;
//	private PeptideBeanSubList peptideSubList;
//	private ProteinBeanSubList proteinSubList;
//	private ProteinGroupBeanSubList proteinGroupSubList;
	private int numTotalPSMs;
	private int numTotalProteins;
	private int numTotalProteinGroups;
	private int numDifferentSequences;
	private int numDifferentSequencesDistinguishingModifieds;
	private List<String> proteinScores;
	private List<String> peptideScores;
	private List<String> psmScores;
	private List<String> ptmScores;
	private List<String> scoreTypes;
	private List<RatioDescriptorBean> ratioDescriptors;

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

//	/**
//	 * @return the psmSubList
//	 */
//	public PsmBeanSubList getPsmSubList() {
//		return psmSubList;
//	}
//
//	/**
//	 * @param psmSubList
//	 *            the psmSubList to set
//	 */
//	public void setPsmSubList(PsmBeanSubList psmSubList) {
//		this.psmSubList = psmSubList;
//	}
//
//	/**
//	 * @return the proteinSubList
//	 */
//	public ProteinBeanSubList getProteinSubList() {
//		return proteinSubList;
//	}
//
//	/**
//	 * @param proteinSubList
//	 *            the proteinSubList to set
//	 */
//	public void setProteinSubList(ProteinBeanSubList proteinSubList) {
//		this.proteinSubList = proteinSubList;
//	}
//
//	/**
//	 * @return the proteinGroupSubList
//	 */
//	public ProteinGroupBeanSubList getProteinGroupSubList() {
//		return proteinGroupSubList;
//	}
//
//	/**
//	 * @param proteinGroupSubList
//	 *            the proteinGroupSubList to set
//	 */
//	public void setProteinGroupSubList(ProteinGroupBeanSubList proteinGroupSubList) {
//		this.proteinGroupSubList = proteinGroupSubList;
//	}

	public boolean isEmpty() {
		return getNumTotalProteins() <= 0;
//		if (proteinSubList != null)
//			return proteinSubList.isEmpty();
//		return true;

	}

	/**
	 * @return the numDifferentSequences
	 */
	public int getNumDifferentSequences() {
		return numDifferentSequences;
	}

	/**
	 * @param numDifferentSequences the numDifferentSequences to set
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
	 * @param numDifferentSequencesDistinguishingModifieds the
	 *                                                     numDifferentSequencesDistinguishingModifieds
	 *                                                     to set
	 */
	public void setNumDifferentSequencesDistinguishingModifieds(int numDifferentSequencesDistinguishingModifieds) {
		this.numDifferentSequencesDistinguishingModifieds = numDifferentSequencesDistinguishingModifieds;
	}

//	/**
//	 * @return the peptideSubList
//	 */
//	public PeptideBeanSubList getPeptideSubList() {
//		return peptideSubList;
//	}

//	/**
//	 * @param peptideSubList
//	 *            the peptideSubList to set
//	 */
//	public void setPeptideSubList(PeptideBeanSubList peptideSubList) {
//		this.peptideSubList = peptideSubList;
//	}

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

	public int getNumTotalPSMs() {
		return numTotalPSMs;
	}

	public void setNumTotalPSMs(int numTotalPSMs) {
		this.numTotalPSMs = numTotalPSMs;
	}

	public int getNumTotalProteins() {
		return numTotalProteins;
	}

	public void setNumTotalProteins(int numTotalProteins) {
		this.numTotalProteins = numTotalProteins;
	}

	public int getNumTotalProteinGroups() {
		return numTotalProteinGroups;
	}

	public void setNumTotalProteinGroups(int numTotalProteinGroups) {
		this.numTotalProteinGroups = numTotalProteinGroups;
	}

	public List<RatioDescriptorBean> getRatioDescriptors() {
		return ratioDescriptors;
	}

	public void setRatioDescriptors(List<RatioDescriptorBean> ratioDescriptors) {
		this.ratioDescriptors = ratioDescriptors;
	}

	public void addRatioDescriptor(RatioDescriptorBean ratioDescriptor) {
		if (this.ratioDescriptors == null) {
			this.ratioDescriptors = new ArrayList<RatioDescriptorBean>();
		}
		ratioDescriptors.add(ratioDescriptor);
	}
}
