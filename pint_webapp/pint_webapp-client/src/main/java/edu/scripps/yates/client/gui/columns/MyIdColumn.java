package edu.scripps.yates.client.gui.columns;

import edu.scripps.yates.shared.model.AmountType;

public interface MyIdColumn<T> extends MyColumn<T> {

	public String getExperimentalConditionName();

	public String getExperimentalCondition2Name();

	public AmountType getAmountType();

	public String getRatioName();

	public String getProjectTag();

	public String getScoreName();

	public String getKeyName();

}
