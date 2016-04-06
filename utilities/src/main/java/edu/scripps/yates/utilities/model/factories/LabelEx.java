package edu.scripps.yates.utilities.model.factories;

import edu.scripps.yates.utilities.proteomicsmodel.Label;

public class LabelEx implements Label {
	private final String name;
	private Double massDiff;

	public LabelEx(String name) {
		super();
		this.name = name;
	}

	/**
	 * @return the massDiff
	 */
	public Double getMassDiff() {
		return massDiff;
	}

	/**
	 * @param massDiff
	 *            the massDiff to set
	 */
	public void setMassDiff(Double massDiff) {
		this.massDiff = massDiff;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

}
