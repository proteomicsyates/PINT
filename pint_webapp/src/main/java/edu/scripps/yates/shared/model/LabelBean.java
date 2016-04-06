package edu.scripps.yates.shared.model;

import java.io.Serializable;

import edu.scripps.yates.client.gui.components.projectCreatorWizard.manager.HasId;

public class LabelBean implements Serializable, HasId {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3332466199547735735L;
	private String name;
	private Double massDiff;

	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

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

	@Override
	public String getId() {
		return name;
	}

}
