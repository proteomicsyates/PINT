package edu.scripps.yates.shared.model;

import java.io.Serializable;

import edu.scripps.yates.client.gui.components.projectCreatorWizard.manager.HasId;

public class SampleBean implements Serializable, HasId {
	/**
	 * @return the serialversionuid
	 */
	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = -5533795123670081466L;
	private String name;
	private String description;
	private boolean wildType;
	private LabelBean label;
	private TissueBean tissue;
	private OrganismBean organism;

	public SampleBean() {

	}

	public void setName(String name) {
		this.name = name;
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
	 * @return the wildType
	 */

	public boolean isWildType() {
		return wildType;
	}

	/**
	 * @param wildType
	 *            the wildType to set
	 */
	public void setWildType(boolean wildType) {
		this.wildType = wildType;
	}

	/**
	 * @return the name
	 */

	@Override
	public String getId() {
		return name;
	}

	/**
	 * @return the labeled
	 */

	public LabelBean getLabel() {
		return label;
	}

	/**
	 * @param labeled
	 *            the labeled to set
	 */
	public void setLabel(LabelBean label) {
		this.label = label;
	}

	/**
	 * @return the tissue
	 */

	public TissueBean getTissue() {
		return tissue;
	}

	/**
	 * @param tissue
	 *            the tissue to set
	 */
	public void setTissue(TissueBean tissue) {
		this.tissue = tissue;
	}

	/**
	 * @return the organism
	 */

	public OrganismBean getOrganism() {
		return organism;
	}

	/**
	 * @param organism2
	 *            the organism to set
	 */
	public void setOrganism(OrganismBean organism2) {
		organism = organism2;
	}

	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */

	@Override
	public String toString() {
		return "SampleEx [name=" + name + ", description=" + description
				+ ", wildType=" + wildType + ", label=" + label + ", tissue="
				+ tissue + ", organism=" + organism + "]";
	}

	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		return -1;
	}

	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof SampleBean) {
			SampleBean sample = (SampleBean) obj;
			if (sample.getId().equals(name))
				return true;
			return false;
		}
		return super.equals(obj);
	}

}
