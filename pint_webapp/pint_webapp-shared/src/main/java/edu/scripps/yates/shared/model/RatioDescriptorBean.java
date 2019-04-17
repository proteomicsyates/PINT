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
	private String proteinScoreName;
	private String peptideScoreName;
	private String psmScoreName;
	private Integer ratioDescriptorID;

	public RatioDescriptorBean() {

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof RatioDescriptorBean) {
			final RatioDescriptorBean ratioDescriptor = (RatioDescriptorBean) obj;
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
	 * @param condition1Name the condition1Name to set
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
	 * @param condition2Name the condition2Name to set
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
	 * @param projectName the projectName to set
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
	 * @param aggregationLevel the aggregationLevel to set
	 */
	public void setAggregationLevel(SharedAggregationLevel aggregationLevel) {
		this.aggregationLevel = aggregationLevel;
	}

	public String getProteinScoreName() {
		return proteinScoreName;
	}

	public void setProteinScoreName(String proteinScoreName) {
		this.proteinScoreName = proteinScoreName;
	}

	public String getPeptideScoreName() {
		return peptideScoreName;
	}

	public void setPeptideScoreName(String peptideScoreName) {
		this.peptideScoreName = peptideScoreName;
	}

	public String getPsmScoreName() {
		return psmScoreName;
	}

	public void setPsmScoreName(String psmScoreName) {
		this.psmScoreName = psmScoreName;
	}

	public void setRatioDescriptorID(int ratioDescriptorID) {
		this.ratioDescriptorID = ratioDescriptorID;
	}

	public Integer getRatioDescriptorID() {
		return ratioDescriptorID;
	}

}
