package edu.scripps.yates.shared.model;

import java.io.Serializable;

import edu.scripps.yates.shared.model.interfaces.HasId;

public class RatioBean extends HasId implements Serializable {

	/**
	 *
	 */
	private static final long serialVersionUID = -1876143014352003478L;
	private int dbID;
	private double value;
	private String description;
	private ScoreBean associatedConfidenceScore;
	private ExperimentalConditionBean condition2;
	private ExperimentalConditionBean condition1;
	private RatioDescriptorBean ratioDescriptorBean;

	public RatioBean() {

	}

	public void setValue(double value) {
		this.value = value;

	}

	public void setDescription(String description) {
		this.description = description;

	}

	public void setAssociatedConfidenceScore(ScoreBean scoreBean) {
		associatedConfidenceScore = scoreBean;
	}

	public void setCondition1(ExperimentalConditionBean conditionBean) {
		condition1 = conditionBean;

	}

	public void setCondition2(ExperimentalConditionBean conditionBean) {
		condition2 = conditionBean;

	}

	/**
	 * @return the value
	 */
	public double getValue() {
		return value;
	}

	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @return the associatedConfidenceScore
	 */
	public ScoreBean getAssociatedConfidenceScore() {
		return associatedConfidenceScore;
	}

	/**
	 * @return the condition2
	 */
	public ExperimentalConditionBean getCondition2() {
		return condition2;
	}

	/**
	 * @return the condition1
	 */
	public ExperimentalConditionBean getCondition1() {
		return condition1;
	}

	/**
	 * @return the dbID
	 */
	public int getDbID() {
		return dbID;
	}

	/**
	 * @param dbID
	 *            the dbID to set
	 */
	public void setDbID(int dbID) {
		this.dbID = dbID;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof RatioBean) {
			final RatioBean ratioBean = (RatioBean) obj;
			if (getCondition1() != null && getCondition1().equals(ratioBean.getCondition1())) {
				if (getCondition2() != null && getCondition2().equals(ratioBean.getCondition2())) {
					if (getDescription() == null
							|| (getDescription() != null && getDescription().equals(ratioBean.getDescription())))
						return true;
				}
			} else {
				if (getCondition1() == null && getCondition2() == null && ratioBean.getCondition1() == null
						&& ratioBean.getCondition2() == null) {
					if (getDescription() == null
							|| (getDescription() != null && getDescription().equals(ratioBean.getDescription())))
						return true;
				}
			}
			return false;
		}
		return super.equals(obj);
	}

	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		return -1;
	}

	@Override
	public String getId() {
		return getDescription();
	}

	/**
	 * Gets a {@link RatioDescriptorBean} object built from the information
	 * contained
	 *
	 * @return
	 */
	public RatioDescriptorBean getRatioDescriptorBean() {
		if (ratioDescriptorBean == null) {
			ratioDescriptorBean = new RatioDescriptorBean();
			if (condition1 != null)
				ratioDescriptorBean.setCondition1Name(condition1.getId());
			if (condition2 != null)
				ratioDescriptorBean.setCondition2Name(condition2.getId());
			if (condition1 != null && condition1.getProject() != null)
				ratioDescriptorBean.setProjectTag(condition1.getProject().getTag());
			ratioDescriptorBean.setRatioName(description);
		}
		return ratioDescriptorBean;
	}

	public void setRatioDescriptorBean(RatioDescriptorBean ratioDescriptorBean2) {
		ratioDescriptorBean = ratioDescriptorBean2;
	}

}
