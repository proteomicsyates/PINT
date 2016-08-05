package edu.scripps.yates.census.read.model;

import edu.scripps.yates.census.analysis.QuantCondition;
import edu.scripps.yates.utilities.model.enums.AmountType;
import edu.scripps.yates.utilities.model.enums.CombinationType;
import edu.scripps.yates.utilities.proteomicsmodel.Amount;
import edu.scripps.yates.utilities.proteomicsmodel.Condition;

public class QuantAmount implements Amount {
	private double value;
	private AmountType amountType;

	private CombinationType combinationType;

	private Boolean singleton = false;

	private Boolean manualSpc = false;
	private QuantCondition quantCondition;

	public QuantAmount(double value, AmountType amountType, QuantCondition condition) {

		this.value = value;
		this.amountType = amountType;
		quantCondition = condition;
	}

	/**
	 * @return the value
	 */

	@Override
	public double getValue() {
		return value;
	}

	/**
	 * @param value
	 *            the value to set
	 */
	public void setValue(double value) {
		this.value = value;
	}

	/**
	 * @return the amountType
	 */

	@Override
	public AmountType getAmountType() {
		return amountType;
	}

	/**
	 * @param amountType
	 *            the amountType to set
	 */
	public void setAmountType(AmountType amountType) {
		this.amountType = amountType;
	}

	/**
	 * @return the combinationType
	 */

	@Override
	public CombinationType getCombinationType() {
		return combinationType;
	}

	/**
	 * @param combinationType
	 *            the combinationType to set
	 */
	public void setCombinationType(CombinationType combinationType) {
		this.combinationType = combinationType;
	}

	/**
	 * @return the singleton
	 */

	@Override
	public Boolean isSingleton() {
		return singleton;
	}

	/**
	 * @param singleton
	 *            the singleton to set
	 */
	public void setSingleton(boolean singleton) {
		this.singleton = singleton;
	}

	/**
	 * @return the manualSpc
	 */

	@Override
	public Boolean isManualSpc() {
		return manualSpc;
	}

	/**
	 * @param manualSpc
	 *            the manualSpc to set
	 */
	public void setManualSpc(boolean manualSpc) {
		this.manualSpc = manualSpc;
	}

	/**
	 * @param condition
	 *            the condition to set
	 */
	public void setQuantCondition(QuantCondition condition) {
		quantCondition = condition;
	}

	@Override
	public Condition getCondition() {
		return quantCondition;
	}

	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof QuantAmount) {
			return toString().equals(obj.toString());
		}
		return super.equals(obj);
	}

	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "QuantAmount [value=" + value + ", amountType=" + amountType + ", combinationType=" + combinationType
				+ ", singleton=" + singleton + ", manualSpc=" + manualSpc + ", quantCondition=" + quantCondition + "]";
	}

}
