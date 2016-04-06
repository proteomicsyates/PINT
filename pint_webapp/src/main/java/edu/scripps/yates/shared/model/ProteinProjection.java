package edu.scripps.yates.shared.model;

import java.io.Serializable;

public class ProteinProjection implements Serializable {
	/**
	 *
	 */
	private static final long serialVersionUID = -6274775666335915012L;
	private String acc;
	private String description;
	private String gene;

	public ProteinProjection() {

	}

	/**
	 * @return the acc
	 */
	public String getAcc() {
		return acc;
	}

	/**
	 * @param acc
	 *            the acc to set
	 */
	public void setAcc(String acc) {
		this.acc = acc;
	}

	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @param description
	 *            the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * @return the gene
	 */
	public String getGene() {
		return gene;
	}

	/**
	 * @param gene
	 *            the gene to set
	 */
	public void setGene(String gene) {
		this.gene = gene;
	}
}
