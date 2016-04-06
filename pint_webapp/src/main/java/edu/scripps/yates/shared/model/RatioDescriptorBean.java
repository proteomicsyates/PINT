package edu.scripps.yates.shared.model;

import java.io.Serializable;

public class RatioDescriptorBean implements Serializable {
	/**
	 *
	 */
	private static final long serialVersionUID = 2371142136202054296L;
	private String condition1Name;
	private String condition2Name;
	private String projectTag;
	private String ratioName;
	private SharedAggregationLevel aggregationLevel;

	public RatioDescriptorBean() {

	}

	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof RatioDescriptorBean) {
			RatioDescriptorBean ratioDescriptor = (RatioDescriptorBean) obj;
			if (ratioDescriptor.getCondition1Name().equals(condition1Name)) {
				if (ratioDescriptor.getCondition2Name().equals(condition2Name)) {
					if (ratioDescriptor.getProjectTag().equals(projectTag)) {
						if (ratioDescriptor.getRatioName().equals(ratioName))
							return true;
					}
				}
			}
		}
		return super.equals(obj);
	}

	/**
	 * @return the condition1Name
	 */
	public String getCondition1Name() {
		return condition1Name;
	}

	/**
	 * @param condition1Name
	 *            the condition1Name to set
	 */
	public void setCondition1Name(String condition1Name) {
		this.condition1Name = condition1Name;
	}

	/**
	 * @return the condition2Name
	 */
	public String getCondition2Name() {
		return condition2Name;
	}

	/**
	 * @param condition2Name
	 *            the condition2Name to set
	 */
	public void setCondition2Name(String condition2Name) {
		this.condition2Name = condition2Name;
	}

	/**
	 * @return the projectName
	 */
	public String getProjectTag() {
		return projectTag;
	}

	/**
	 * @param projectName
	 *            the projectName to set
	 */
	public void setProjectTag(String projectTag) {
		this.projectTag = projectTag;
	}

	public void setRatioName(String ratioName) {
		this.ratioName = ratioName;

	}

	/**
	 * @return the ratioName
	 */
	public String getRatioName() {
		return ratioName;
	}

	/**
	 * @return the aggregationLevel
	 */
	public SharedAggregationLevel getAggregationLevel() {
		return aggregationLevel;
	}

	/**
	 * @param aggregationLevel
	 *            the aggregationLevel to set
	 */
	public void setAggregationLevel(SharedAggregationLevel aggregationLevel) {
		this.aggregationLevel = aggregationLevel;
	}

}
