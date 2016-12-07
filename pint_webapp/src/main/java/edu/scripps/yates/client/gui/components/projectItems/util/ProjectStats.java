package edu.scripps.yates.client.gui.components.projectItems.util;

public abstract class ProjectStats<T> {
	private Integer numConditions;
	private Integer numMSRuns;
	protected Integer numSamples;
	private Integer numProteins;
	private Integer numPeptides;
	private Integer numPSMs;
	private Integer numGenes;
	protected final T t;

	public ProjectStats(T t) {
		this.t = t;
	}

	/**
	 * @return the numConditions
	 */
	public Integer getNumConditions() {
		return numConditions;
	}

	/**
	 * @return the numMSRuns
	 */
	public Integer getNumMSRuns() {
		return numMSRuns;
	}

	/**
	 * @param numMSRuns
	 *            the numMSRuns to set
	 */
	public void setNumMSRuns(Integer numMSRuns) {
		this.numMSRuns = numMSRuns;
	}

	/**
	 * @return the numSamples
	 */
	public Integer getNumSamples() {
		return numSamples;
	}

	/**
	 * @return the numProteins
	 */
	public Integer getNumProteins() {
		return numProteins;
	}

	/**
	 * @param numProteins
	 *            the numProteins to set
	 */
	public void setNumProteins(Integer numProteins) {
		this.numProteins = numProteins;
	}

	/**
	 * @return the numPeptides
	 */
	public Integer getNumPeptides() {
		return numPeptides;
	}

	/**
	 * @param numPeptides
	 *            the numPeptides to set
	 */
	public void setNumPeptides(Integer numPeptides) {
		this.numPeptides = numPeptides;
	}

	/**
	 * @return the numPSMs
	 */
	public Integer getNumPSMs() {
		return numPSMs;
	}

	/**
	 * @param numPSMs
	 *            the numPSMs to set
	 */
	public void setNumPSMs(Integer numPSMs) {
		this.numPSMs = numPSMs;
	}

	public T getT() {
		return t;
	}

	/**
	 * @return the numGenes
	 */
	public Integer getNumGenes() {
		return numGenes;
	}

	/**
	 * @param numGenes
	 *            the numGenes to set
	 */
	public void setNumGenes(Integer numGenes) {
		this.numGenes = numGenes;
	}

	public void setNumConditions(Integer numConditions) {
		this.numConditions = numConditions;
	}

	public void setNumSamples(Integer numSamples) {
		this.numSamples = numSamples;
	}
}
