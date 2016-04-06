package edu.scripps.yates.utilities.model.factories;

import java.io.Serializable;

import edu.scripps.yates.utilities.proteomicsmodel.Tissue;

public class TissueEx implements Tissue, Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -8230799076830462784L;
	private final String tissueID;
	private String name;

	public TissueEx(String tissueID) {
		super();
		this.tissueID = tissueID;
	}

	/**
	 * @return the tissueID
	 */
	@Override
	public String getTissueID() {
		return tissueID;
	}

	/**
	 * @return the name
	 */
	@Override
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

	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "TissueEx [tissueID=" + tissueID + ", name=" + name + "]";
	}

}
