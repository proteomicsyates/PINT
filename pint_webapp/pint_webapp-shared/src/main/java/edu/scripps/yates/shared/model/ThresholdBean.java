package edu.scripps.yates.shared.model;

import java.io.Serializable;

/**
 * Class that represents a threshold.
 * 
 * @author Salva
 * 
 */
public class ThresholdBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -914002851522087374L;
	private String name;
	private String description;

	public boolean pass;

	public ThresholdBean() {
		super();

	}

	/**
	 * @return the name
	 */

	public String getName() {
		return name;
	}

	/**
	 * @return the description
	 */

	public String getDescription() {
		return description;
	}

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

	/**
	 * @return the pass
	 */
	public boolean isPass() {
		return pass;
	}

	/**
	 * @param pass
	 *            the pass to set
	 */
	public void setPass(boolean pass) {
		this.pass = pass;
	}

	/**
	 * @param name
	 *            the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}
}
