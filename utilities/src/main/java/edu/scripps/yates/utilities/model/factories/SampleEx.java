package edu.scripps.yates.utilities.model.factories;

import java.io.Serializable;

import edu.scripps.yates.utilities.proteomicsmodel.Label;
import edu.scripps.yates.utilities.proteomicsmodel.Organism;
import edu.scripps.yates.utilities.proteomicsmodel.Sample;
import edu.scripps.yates.utilities.proteomicsmodel.Tissue;

public class SampleEx implements Sample, Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -5533795123670081466L;
	private final String name;
	private String description;
	private Boolean wildType;
	private Label label;
	private Tissue tissue;
	private Organism organism;

	public SampleEx(String name) {

		this.name = name;
	}

	/**
	 * @return the description
	 */
	@Override
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
	@Override
	public Boolean isWildType() {
		return wildType;
	}

	/**
	 * @param wildType
	 *            the wildType to set
	 */
	public void setWildType(Boolean wildType) {
		this.wildType = wildType;
	}

	/**
	 * @return the name
	 */
	@Override
	public String getName() {
		return name;
	}

	/**
	 * @return the labeled
	 */
	@Override
	public Label getLabel() {
		return label;
	}

	/**
	 * @param labeled
	 *            the labeled to set
	 */
	public void setLabel(Label label) {
		this.label = label;
	}

	/**
	 * @return the tissue
	 */
	@Override
	public Tissue getTissue() {
		return tissue;
	}

	/**
	 * @param tissue
	 *            the tissue to set
	 */
	public void setTissue(Tissue tissue) {
		this.tissue = tissue;
	}

	/**
	 * @return the organism
	 */
	@Override
	public Organism getOrganism() {
		return organism;
	}

	/**
	 * @param organism2
	 *            the organism to set
	 */
	public void setOrganism(Organism organism2) {
		organism = organism2;
	}

	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "SampleEx [name=" + name + ", description=" + description
				+ ", wildType=" + wildType + ", labeled=" + label + ", tissue="
				+ tissue + ", organism=" + organism + "]";
	}

}
