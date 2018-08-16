package edu.scripps.yates.proteindb.persistence.mysql.wrappers;

import edu.scripps.yates.proteindb.persistence.mysql.AmountType;
import edu.scripps.yates.proteindb.persistence.mysql.CombinationType;

public class AmountValueWrapper {
	private final int id;
	private final double value;
	private final int itemID;
	private final AmountType amountType;
	private final CombinationType combinationType;
	private final Boolean manualSPC;
	private final int conditionID;

	public AmountValueWrapper(int id, double value, int itemID, AmountType amountType, CombinationType combinationType,
			Boolean manualSPC, int conditionID) {
		this.id = id;
		this.value = value;
		this.itemID = itemID;
		this.amountType = amountType;
		this.combinationType = combinationType;
		this.manualSPC = manualSPC;
		this.conditionID = conditionID;
	}

	public int getId() {
		return id;
	}

	public double getValue() {
		return value;
	}

	public int getItemID() {
		return itemID;
	}

	public AmountType getAmountType() {
		return amountType;
	}

	public CombinationType getCombinationType() {
		return combinationType;
	}

	public Boolean getManualSPC() {
		return manualSPC;
	}

	public int getConditionID() {
		return conditionID;
	}

}
