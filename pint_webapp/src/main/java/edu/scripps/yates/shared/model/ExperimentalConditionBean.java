package edu.scripps.yates.shared.model;

import java.io.Serializable;

import edu.scripps.yates.shared.model.interfaces.HasId;

public class ExperimentalConditionBean extends HasId implements Serializable {

	/**
	 *
	 */
	private static final long serialVersionUID = 5756109096565698753L;
	private String name;
	private String description;
	private String unit;
	private Double value;
	private SampleBean sample;
	private ProjectBean project;

	public ExperimentalConditionBean() {

	}

	/**
	 * @return the serialversionuid
	 */
	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public void setName(String name) {
		this.name = name;

	}

	public void setDescription(String description) {
		this.description = description;

	}

	public void setUnit(String unit) {
		this.unit = unit;

	}

	public void setValue(Double value) {
		this.value = value;

	}

	public void setSample(SampleBean sampleBean) {
		sample = sampleBean;

	}

	/**
	 * @return the name
	 */
	@Override
	public String getId() {
		return name;
	}

	@Override
	public String toString() {
		return getId();
	}

	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @return the unit
	 */
	public String getUnit() {
		return unit;
	}

	/**
	 * @return the value
	 */
	public Double getValue() {
		return value;
	}

	/**
	 * @return the sample
	 */
	public SampleBean getSample() {
		return sample;
	}

	public ProjectBean getProject() {
		return project;
	}

	public void setProject(ProjectBean project) {
		this.project = project;
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
		if (obj instanceof ExperimentalConditionBean) {
			ExperimentalConditionBean condition = (ExperimentalConditionBean) obj;
			if (condition.getId().equals(name))
				return true;
			return false;
		}
		return super.equals(obj);
	}

}
