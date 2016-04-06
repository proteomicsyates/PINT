package edu.scripps.yates.shared.model.projectCreator.excel;

import java.io.Serializable;

public class FastaDigestionBean implements Serializable {

	/**
	 *
	 */
	private static final long serialVersionUID = 4937176905832808969L;
	protected String cleavageAAs;
	protected int misscleavages;
	protected int enzymeOffset;
	protected String enzymeNoCutResidues;
	protected boolean isH2OPlusProtonAdded;

	/**
	 * Gets the value of the cleavageAAs property.
	 *
	 * @return possible object is {@link String }
	 *
	 */
	public String getCleavageAAs() {
		return cleavageAAs;
	}

	/**
	 * Sets the value of the cleavageAAs property.
	 *
	 * @param value
	 *            allowed object is {@link String }
	 *
	 */
	public void setCleavageAAs(String value) {
		cleavageAAs = value;
	}

	/**
	 * Gets the value of the misscleavages property.
	 *
	 */
	public int getMisscleavages() {
		return misscleavages;
	}

	/**
	 * Sets the value of the misscleavages property.
	 *
	 */
	public void setMisscleavages(int value) {
		misscleavages = value;
	}

	/**
	 * Gets the value of the enzymeOffset property.
	 *
	 */
	public int getEnzymeOffset() {
		return enzymeOffset;
	}

	/**
	 * Sets the value of the enzymeOffset property.
	 *
	 */
	public void setEnzymeOffset(int value) {
		enzymeOffset = value;
	}

	/**
	 * Gets the value of the enzymeNoCutResidues property.
	 *
	 * @return possible object is {@link String }
	 *
	 */
	public String getEnzymeNoCutResidues() {
		return enzymeNoCutResidues;
	}

	/**
	 * Sets the value of the enzymeNoCutResidues property.
	 *
	 * @param value
	 *            allowed object is {@link String }
	 *
	 */
	public void setEnzymeNoCutResidues(String value) {
		enzymeNoCutResidues = value;
	}

	/**
	 * Gets the value of the isH2OPlusProtonAdded property.
	 *
	 */
	public boolean isIsH2OPlusProtonAdded() {
		return isH2OPlusProtonAdded;
	}

	/**
	 * Sets the value of the isH2OPlusProtonAdded property.
	 *
	 */
	public void setIsH2OPlusProtonAdded(boolean value) {
		isH2OPlusProtonAdded = value;
	}

	/**
	 * @return the isH2OPlusProtonAdded
	 */
	public boolean isH2OPlusProtonAdded() {
		return isH2OPlusProtonAdded;
	}

	/**
	 * @param isH2OPlusProtonAdded
	 *            the isH2OPlusProtonAdded to set
	 */
	public void setH2OPlusProtonAdded(boolean isH2OPlusProtonAdded) {
		this.isH2OPlusProtonAdded = isH2OPlusProtonAdded;
	}

	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof FastaDigestionBean) {
			FastaDigestionBean fastaDigestionBean = (FastaDigestionBean) obj;
			if (!fastaDigestionBean.getCleavageAAs().equals(cleavageAAs))
				return false;
			if (!fastaDigestionBean.getEnzymeNoCutResidues().equals(enzymeNoCutResidues))
				return false;
			if (fastaDigestionBean.getEnzymeOffset() != enzymeOffset)
				return false;
			if (fastaDigestionBean.getMisscleavages() != misscleavages)
				return false;
			if (Boolean.compare(fastaDigestionBean.isH2OPlusProtonAdded, isH2OPlusProtonAdded) != 0)
				return false;

			return true;
		}
		return super.equals(obj);
	}

}
