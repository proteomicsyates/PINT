package edu.scripps.yates.shared.model.interfaces;

import java.util.HashMap;
import java.util.List;

import edu.scripps.yates.shared.model.AmountBean;

public interface ContainsAmounts {
	public List<AmountBean> getAmounts();

	public boolean hasCombinationAmounts(String conditionName, String projectName);

	public List<AmountBean> getCombinationAmount(String conditionName, String projectName);

	public List<AmountBean> getNonCombinationAmounts(String conditionName, String projectName);

	public HashMap<String, List<AmountBean>> getAmountsByExperimentalCondition();

}
