package edu.scripps.yates.utilities.model.factories;

import java.io.Serializable;

import edu.scripps.yates.utilities.proteomicsmodel.Threshold;

/**
 * Class that represents a threshold.
 * 
 * @author Salva
 * 
 */
public class ThresholdEx implements Threshold, Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -914002851522087374L;
	private final String name;
	private String description;

	public final boolean pass;

	public ThresholdEx(String name, boolean pass) {
		super();
		this.name = name;
		this.pass = pass;
	}

	/**
	 * @return the name
	 */
	@Override
	public String getName() {
		return name;
	}

	/**
	 * @return the description
	 */
	@Override
	public String getDescription() {
		return description;
	}

	@Override
	public boolean isPassThreshold() {
		return pass;
	}

	/**
	 * @param description
	 *            the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}
}
