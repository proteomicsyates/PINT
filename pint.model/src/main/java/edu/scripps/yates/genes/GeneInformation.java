package edu.scripps.yates.genes;

import java.io.Serializable;

public class GeneInformation implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -3060595028984605833L;
	private int hgncId;
	private String approvedSymbol;
	private String approvedName;
	private String status;
	private String previousSymbols;

	private String previousNames;
	private String synonyms;
	private String chromosome;
	private String accessionNumbers;
	private String refSeqIDs;

	public GeneInformation() {

	}

	/**
	 * @return the approvedSymbol
	 */
	public String getApprovedSymbol() {
		return approvedSymbol;
	}

	/**
	 * @param approvedSymbol
	 *            the approvedSymbol to set
	 */
	public void setApprovedSymbol(String approvedSymbol) {
		this.approvedSymbol = approvedSymbol;
	}

	/**
	 * @return the approvedName
	 */
	public String getApprovedName() {
		return approvedName;
	}

	/**
	 * @param approvedName
	 *            the approvedName to set
	 */
	public void setApprovedName(String approvedName) {
		this.approvedName = approvedName;
	}

	/**
	 * @return the status
	 */
	public String getStatus() {
		return status;
	}

	/**
	 * @param status
	 *            the status to set
	 */
	public void setStatus(String status) {
		this.status = status;
	}

	/**
	 * @return the previousSymbols
	 */
	public String getPreviousSymbols() {
		return previousSymbols;
	}

	/**
	 * @param previousSymbols
	 *            the previousSymbols to set
	 */
	public void setPreviousSymbols(String previousSymbols) {
		this.previousSymbols = previousSymbols;
	}

	/**
	 * @return the previousNames
	 */
	public String getPreviousNames() {
		return previousNames;
	}

	/**
	 * @param previousNames
	 *            the previousNames to set
	 */
	public void setPreviousNames(String previousNames) {
		this.previousNames = previousNames;
	}

	/**
	 * @return the synonyms
	 */
	public String getSynonyms() {
		return synonyms;
	}

	/**
	 * @param synonyms
	 *            the synonyms to set
	 */
	public void setSynonyms(String synonyms) {
		this.synonyms = synonyms;
	}

	/**
	 * @return the chromosome
	 */
	public String getChromosome() {
		return chromosome;
	}

	/**
	 * @param chromosome
	 *            the chromosome to set
	 */
	public void setChromosome(String chromosome) {
		this.chromosome = chromosome;
	}

	/**
	 * @return the accessionNumbers
	 */
	public String getAccessionNumbers() {
		return accessionNumbers;
	}

	/**
	 * @param accessionNumbers
	 *            the accessionNumbers to set
	 */
	public void setAccessionNumbers(String accessionNumbers) {
		this.accessionNumbers = accessionNumbers;
	}

	/**
	 * @return the refSeqIDs
	 */
	public String getRefSeqIDs() {
		return refSeqIDs;
	}

	/**
	 * @param refSeqIDs
	 *            the refSeqIDs to set
	 */
	public void setRefSeqIDs(String refSeqIDs) {
		this.refSeqIDs = refSeqIDs;
	}

	/**
	 * @return the hgncId
	 */
	public int getHgncId() {
		return hgncId;
	}

	/**
	 * @param hgncId
	 *            the hgncId to set
	 */
	public void setHgncId(int hgncId) {
		this.hgncId = hgncId;
	}

	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "GeneInformation [hgncId=" + hgncId + ", approvedSymbol="
				+ approvedSymbol + ", approvedName=" + approvedName
				+ ", status=" + status + ", previousSymbols=" + previousSymbols
				+ ", previousNames=" + previousNames + ", synonyms=" + synonyms
				+ ", chromosome=" + chromosome + ", accessionNumbers="
				+ accessionNumbers + ", refSeqIDs=" + refSeqIDs + "]";
	}
}
