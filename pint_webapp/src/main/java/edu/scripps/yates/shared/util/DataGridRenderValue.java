package edu.scripps.yates.shared.util;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;

import edu.scripps.yates.client.util.ClientMaths;
import edu.scripps.yates.shared.model.AmountBean;
import edu.scripps.yates.shared.model.AmountType;
import edu.scripps.yates.shared.model.interfaces.ContainsAmounts;

/**
 * Represents the value and the tooltip of a datagrid cell
 *
 * @author Salva
 *
 */
public class DataGridRenderValue implements Serializable {
	/**
	 *
	 */
	private static final long serialVersionUID = -3718837732346843195L;
	private String value;
	private String tooltip;
	private Double actualNonRoundedValue;
	private static final NumberFormat twoDecimalsFormat = NumberFormat.getFormat("#.##");
	private static final NumberFormat threeDecimalsFormat = NumberFormat.getFormat("#.##");
	private static final NumberFormat scientificDecimalsFormat = NumberFormat.getScientificFormat();

	public DataGridRenderValue(String value, Double actualNonRoundedValue, String tooltip) {
		this.value = value;
		this.tooltip = tooltip;
		setActualNonRoundedValue(actualNonRoundedValue);
	}

	/**
	 * @return the value
	 */
	public String getValue() {
		return value;
	}

	/**
	 * @return the value
	 */
	public SafeHtml getValueAsSafeHtml() {
		return new SafeHtmlBuilder().appendEscapedLines(value).toSafeHtml();
	}

	/**
	 * @return the tooltip
	 */
	public String getTooltip() {
		return tooltip;
	}

	/**
	 * @param value
	 *            the value to set
	 */
	public void setValue(String value) {
		this.value = value;
	}

	/**
	 * @param tooltip
	 *            the tooltip to set
	 */
	public void setTooltip(String tooltip) {
		this.tooltip = tooltip;
	}

	public static DataGridRenderValue getAmountDataGridRenderValue(ContainsAmounts p, String conditionName,
			AmountType amountType, String projectTag) {

		StringBuilder combination = new StringBuilder();

		StringBuilder tooltip = new StringBuilder();
		Double combinationNotRounded = null;
		if (p.hasCombinationAmounts(conditionName, projectTag)) {
			StringBuilder nonCombination = new StringBuilder();
			List<AmountBean> combinationAmounts = p.getCombinationAmount(conditionName, projectTag);
			Set<String> combinationStringSet = new HashSet<String>();
			for (AmountBean combinationAmount : combinationAmounts) {
				if (combinationAmount.getAmountType() == amountType) {
					String combinationString = amountType.getDescription() + combinationAmount.getValue();
					if (!combinationStringSet.contains(combinationString)) {
						combinationStringSet.add(combinationString);
						if (!"".equals(combination.toString())) {
							combination.append(", ");
						}
						combination.append(String.valueOf(combinationAmount.getValue()));
					}
				}
			}
			tooltip.append("Experimental condition: " + conditionName + SharedConstants.NEW_LINE_JAVA
					+ amountType.getDescription() + SharedConstants.NEW_LINE_JAVA + nonCombination.toString());
			return new DataGridRenderValue(combination.toString(), combinationNotRounded, tooltip.toString());
		} else {
			tooltip.append("Experimental condition: " + conditionName);

			final List<AmountBean> nonCombinationAmounts = p.getNonCombinationAmounts(conditionName, projectTag);
			if (nonCombinationAmounts != null && !nonCombinationAmounts.isEmpty()) {
				Set<AmountBean> amounts = getAmountsByType(nonCombinationAmounts).get(amountType);

				StringBuilder nonCombination = new StringBuilder();
				List<Double> total = new ArrayList<Double>();
				if (amounts != null) {
					tooltip.append(SharedConstants.SEPARATOR + amountType);
					for (AmountBean amountBean : amounts) {
						if (!"".equals(nonCombination.toString())) {
							nonCombination.append(SharedConstants.SEPARATOR);
						}
						nonCombination.append(amountBean.getValue());
						if (amountBean.getMsRun() != null) {
							nonCombination.append(" ('" + amountBean.getMsRun().getRunID() + "')");
						}
						total.add(amountBean.getValue());
					}
					tooltip.append(SharedConstants.SEPARATOR + nonCombination);
				}
				if (!"".equals(combination.toString())) {
					combination.append(SharedConstants.SEPARATOR);
				}
				// actual final value of the amount
				if (amountType == AmountType.SPC) {
					// sum
					combinationNotRounded = ClientMaths.sum(total.toArray(new Double[0]));
					combination.append(formatDouble(combinationNotRounded));
				} else {
					// mean
					combinationNotRounded = ClientMaths.mean(total.toArray(new Double[0]));
					combination.append(formatDouble(combinationNotRounded));
				}

				return new DataGridRenderValue(combination.toString(), combinationNotRounded, tooltip.toString());
			}
		}
		return new DataGridRenderValue("-", null, "");
	}

	private static Map<AmountType, Set<AmountBean>> getAmountsByType(List<AmountBean> amounts) {
		Map<AmountType, Set<AmountBean>> map = new HashMap<AmountType, Set<AmountBean>>();
		for (AmountBean amountBean : amounts) {
			if (!map.containsKey(amountBean.getAmountType())) {
				Set<AmountBean> set = new HashSet<AmountBean>();
				set.add(amountBean);
				map.put(amountBean.getAmountType(), set);
			} else {
				map.get(amountBean.getAmountType()).add(amountBean);
			}
		}
		return map;
	}

	/**
	 * @return the actualNonRoundedValue
	 */
	public Double getActualNonRoundedValue() {
		return actualNonRoundedValue;
	}

	/**
	 * @param actualNonRoundedValue
	 *            the actualNonRoundedValue to set
	 */
	public void setActualNonRoundedValue(Double actualNonRoundedValue) {
		this.actualNonRoundedValue = actualNonRoundedValue;
	}

	private static String formatDouble(Double number) {
		if (number >= 0.01) {
			return twoDecimalsFormat.format(number);
		} else {

			return scientificDecimalsFormat.format(number);

		}
	}
}
