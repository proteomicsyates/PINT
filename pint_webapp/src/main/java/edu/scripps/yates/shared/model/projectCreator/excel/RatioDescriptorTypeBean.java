package edu.scripps.yates.shared.model.projectCreator.excel;

import java.io.Serializable;

import edu.scripps.yates.client.gui.components.projectCreatorWizard.manager.HasId;

public class RatioDescriptorTypeBean implements Serializable, HasId {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4133915123931895557L;
	/**
	 * 
	 */
	private String description;
	private ExperimentalConditionTypeBean condition2;
	private ExperimentalConditionTypeBean condition1;

	public RatioDescriptorTypeBean() {

	}

	public void setDescription(String description) {
		this.description = description;

	}

	public void setCondition1(ExperimentalConditionTypeBean conditionBean) {
		condition1 = conditionBean;

	}

	public void setCondition2(ExperimentalConditionTypeBean conditionBean) {
		condition2 = conditionBean;

	}

	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @return the condition2
	 */
	public ExperimentalConditionTypeBean getCondition2() {
		return condition2;
	}

	/**
	 * @return the condition1
	 */
	public ExperimentalConditionTypeBean getCondition1() {
		return condition1;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof RatioDescriptorTypeBean) {
			final RatioDescriptorTypeBean ratioBean = (RatioDescriptorTypeBean) obj;
			if (getCondition1() != null
					&& getCondition1().equals(ratioBean.getCondition1())) {
				if (getCondition2() != null
						&& getCondition2().equals(ratioBean.getCondition2())) {
					if (getDescription() == null
							|| (getDescription() != null && getDescription()
									.equals(ratioBean.getDescription())))
						return true;
				}
			} else {
				if (getCondition1() == null && getCondition2() == null
						&& ratioBean.getCondition1() == null
						&& ratioBean.getCondition2() == null) {
					if (getDescription() == null
							|| (getDescription() != null && getDescription()
									.equals(ratioBean.getDescription())))
						return true;
				}
			}
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

}
