package edu.scripps.yates.shared.model;

import java.io.Serializable;

public class GeneBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7508538437194587281L;
	public static final String PRIMARY = "primary";
	private String geneID;
	private String geneType;
	private int hgncNumber;
	private String name;

	public GeneBean() {

	}

	public void setGeneType(String geneType) {
		this.geneType = geneType;
	}

	/**
	 * @return the geneID
	 */
	public String getGeneID() {
		return geneID;
	}

	/**
	 * @param geneID
	 *            the geneID to set
	 */
	public void setGeneID(String geneID) {
		this.geneID = geneID;
	}

	/**
	 * @return the geneType
	 */
	public String getGeneType() {
		return geneType;
	}

	/**
	 * @return the hgncNumber
	 */
	public int getHgncNumber() {
		return hgncNumber;
	}

	/**
	 * @param hgncNumber
	 *            the hgncNumber to set
	 */
	public void setHgncNumber(int hgncNumber) {
		this.hgncNumber = hgncNumber;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name
	 *            the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

}
