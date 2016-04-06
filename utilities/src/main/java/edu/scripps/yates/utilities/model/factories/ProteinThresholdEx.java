package edu.scripps.yates.utilities.model.factories;

import java.io.Serializable;

import edu.scripps.yates.utilities.proteomicsmodel.Threshold;

public class ProteinThresholdEx implements Threshold, Serializable {

	private static final long serialVersionUID = -2203075976390368915L;
	private String name;
	private boolean passThreshold;
	private String description;

	public ProteinThresholdEx(String name, String description, boolean passThreshold) {
		this.name = name;
		this.passThreshold = passThreshold;
		this.description = description;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public String getDescription() {
		return description;
	}

	@Override
	public boolean isPassThreshold() {
		return passThreshold;
	}

	/**
	 * @param name
	 *            the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @param passThreshold
	 *            the passThreshold to set
	 */
	public void setPassThreshold(boolean passThreshold) {
		this.passThreshold = passThreshold;
	}

	/**
	 * @param description
	 *            the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}

}
