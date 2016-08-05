package edu.scripps.yates.utilities.model.factories;

import java.io.Serializable;
import java.util.Collection;

import edu.scripps.yates.utilities.model.enums.AmountType;
import edu.scripps.yates.utilities.model.enums.CombinationType;
import edu.scripps.yates.utilities.proteomicsmodel.Amount;
import edu.scripps.yates.utilities.proteomicsmodel.Condition;
import edu.scripps.yates.utilities.proteomicsmodel.Protein;

public class AmountEx implements Amount, Serializable {

	/**
	 *
	 */
	private static final long serialVersionUID = -6291033827811837759L;
	/**
	 * can be null and in that case, it means that it was discarded for any
	 * reason.
	 */
	private final double value;
	private final AmountType amountType;
	private final Condition condition;
	// optional
	private CombinationType combinationType;
	private Boolean singleton;
	private Boolean manualSpc;

	public AmountEx(double value, AmountType amountType, Condition condition) {
		this.value = value;
		this.amountType = amountType;
		this.condition = condition;
	}

	/**
	 * Constructor for a {@link Amount} coming from the combination of a
	 * {@link Collection} of other {@link Amount}.<br>
	 * The {@link Condition} associated with these {@link Amount} must be the
	 * same for all of them.<br>
	 * The {@link Protein} referenced by these {@link Amount} must be the same
	 * for all of them.
	 *
	 * @param value
	 * @param amountType
	 * @param replicatesAmounts
	 */
	public AmountEx(double value, AmountType amountType, CombinationType combinationType, Condition condition) {
		this.value = value;
		this.amountType = amountType;
		this.combinationType = combinationType;
		this.condition = condition;

	}

	/**
	 * @return the value
	 */
	@Override
	public double getValue() {
		return value;
	}

	/**
	 * @return the amountType
	 */
	@Override
	public AmountType getAmountType() {
		return amountType;
	}

	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		String string = "AmountEx [value=" + value + ", amountType=" + amountType;
		if (combinationType != null)
			string += ", combinationType=" + combinationType + " ";
		if (singleton != null)
			string += ", singleton=" + singleton + " ";
		string += "]";
		return string;
	}

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

	@Override
	public Condition getCondition() {
		return condition;
	}

	@Override
	public Boolean isSingleton() {
		return singleton;
	}

	/**
	 * @return the singleton
	 */
	public Boolean getSingleton() {
		return singleton;
	}

	/**
	 * @param singleton
	 *            the singleton to set
	 */
	public void setSingleton(Boolean singleton) {
		this.singleton = singleton;
	}

	/**
	 * @return the manualSpc
	 */
	public Boolean getManualSpc() {
		return manualSpc;
	}

	/**
	 * @param manualSpc
	 *            the manualSpc to set
	 */
	public void setManualSpc(Boolean manualSpc) {
		this.manualSpc = manualSpc;
	}

	@Override
	public Boolean isManualSpc() {
		return manualSpc;
	}

}