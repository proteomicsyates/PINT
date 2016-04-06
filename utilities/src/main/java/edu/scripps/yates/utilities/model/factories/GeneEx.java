package edu.scripps.yates.utilities.model.factories;

import java.io.Serializable;

import edu.scripps.yates.utilities.proteomicsmodel.Gene;

public class GeneEx implements Gene, Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1784866680972113426L;
	private final String geneID;
	private String geneType;

	public GeneEx(String geneId) {
		if (geneId == null || "".equals(geneId))
			throw new IllegalArgumentException("gene id is null!");
		geneID = geneId;
	}

	@Override
	public String getGeneID() {
		return geneID;
	}

	@Override
	public String getGeneType() {
		return geneType;
	}

	/**
	 * @param geneType
	 *            the geneType to set
	 */
	public void setGeneType(String geneType) {
		this.geneType = geneType;
	}

}
