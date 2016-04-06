package edu.scripps.yates.shared.model;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class AmountBean implements Serializable {

	/**
	 *
	 */
	private static final long serialVersionUID = -7477527761355445596L;
	private double value;
	private AmountType amountType;
	private ExperimentalConditionBean experimentalCondition;
	private boolean isComposed = false;

	private MSRunBean msRun;
	private Boolean manualSPC;

	public AmountBean() {

	}

	public void setValue(double value) {
		this.value = value;

	}

	public void setAmountType(AmountType amountType) {
		this.amountType = amountType;
	}

	public void setExperimentalCondition(ExperimentalConditionBean conditionBean) {
		experimentalCondition = conditionBean;

	}

	/**
	 * @return the value
	 */
	public double getValue() {
		return value;
	}

	/**
	 * @return the amountType
	 */
	public AmountType getAmountType() {
		return amountType;
	}

	/**
	 * @return the experimentalCondition
	 */
	public ExperimentalConditionBean getExperimentalCondition() {
		return experimentalCondition;
	}

	/**
	 * @return the isComposed
	 */
	public boolean isComposed() {
		return isComposed;
	}

	/**
	 * @param isComposed
	 *            the isComposed to set
	 */
	public void setComposed(boolean isComposed) {
		this.isComposed = isComposed;
	}

	public static Set<AmountBean> getComposedAmounts(Collection<AmountBean> amounts) {
		Set<AmountBean> ret = new HashSet<AmountBean>();
		for (AmountBean amount : amounts) {
			if (amount.isComposed())
				ret.add(amount);
		}
		return ret;

	}

	/**
	 * @return the msRun
	 */
	public MSRunBean getMsRun() {
		return msRun;
	}

	/**
	 * @param msRun
	 *            the msRun to set
	 */
	public void setMsRun(MSRunBean msRun) {
		this.msRun = msRun;
	}

	public void setManualSPC(Boolean manualSPC) {
		this.manualSPC = manualSPC;

	}

	/**
	 * @return the manualSPC
	 */
	public Boolean getManualSPC() {
		return manualSPC;
	}

}
