package edu.scripps.yates.shared.columns.comparator;

import java.io.Serializable;
import java.util.Comparator;
import java.util.List;

import edu.scripps.yates.shared.columns.ColumnName;
import edu.scripps.yates.shared.model.AmountType;
import edu.scripps.yates.shared.model.RatioBean;
import edu.scripps.yates.shared.model.ScoreBean;
import edu.scripps.yates.shared.model.interfaces.ContainsRatios;
import edu.scripps.yates.shared.util.SharedDataUtils;

public abstract class BeanComparator<T> implements Comparator<T>, Serializable {
	/**
	 *
	 */
	private static final long serialVersionUID = 6993812445713528073L;
	ColumnName columnName;
	String conditionName;
	// in case of ratios
	String condition2Name;
	String projectTag;
	String ratioName;
	AmountType amountType;
	String scoreName;
	boolean ascendant;

	public BeanComparator() {
		columnName = null;
		conditionName = null;
		condition2Name = null;
		projectTag = null;
		ratioName = null;
		amountType = null;
		this.scoreName = null;
	};

	public BeanComparator(ColumnName columnName) {
		this.columnName = columnName;
		conditionName = null;
		condition2Name = null;
		projectTag = null;
		ratioName = null;
		amountType = null;
		scoreName = null;
	}

	public BeanComparator(ColumnName columnName, String experimentalConditionName, AmountType amountType,
			String projectTag) {
		this.columnName = columnName;
		conditionName = experimentalConditionName;
		condition2Name = null;
		this.projectTag = projectTag;
		ratioName = null;
		this.amountType = amountType;
		this.scoreName = null;

	}

	public BeanComparator(ColumnName columnName, String scoreName) {
		this.columnName = columnName;
		conditionName = null;
		condition2Name = null;
		this.projectTag = null;
		ratioName = null;
		this.amountType = null;
		this.scoreName = scoreName;
	}

	public BeanComparator(ColumnName columnName, String experimentalCondition1Name, String experimentalCondition2Name,
			String projectTag, String ratioName) {
		this.columnName = columnName;
		conditionName = experimentalCondition1Name;
		condition2Name = experimentalCondition2Name;
		this.projectTag = projectTag;
		this.ratioName = ratioName;
		amountType = null;
		this.scoreName = null;
	}

	/**
	 * @return the columnName
	 */
	public ColumnName getColumnName() {
		return columnName;
	}

	/**
	 * @param columnName
	 *            the columnName to set
	 */
	public void setColumnName(ColumnName columnName) {
		this.columnName = columnName;
	}

	/**
	 * @return the conditionName
	 */
	public String getConditionName() {
		return conditionName;
	}

	/**
	 * @param conditionName
	 *            the conditionName to set
	 */
	public void setConditionName(String conditionName) {
		this.conditionName = conditionName;
	}

	/**
	 * @return the condition2Name
	 */
	public String getCondition2Name() {
		return condition2Name;
	}

	/**
	 * @param condition2Name
	 *            the condition2Name to set
	 */
	public void setCondition2Name(String condition2Name) {
		this.condition2Name = condition2Name;
	}

	/**
	 * @return the projectTag
	 */
	public String getProjectTag() {
		return projectTag;
	}

	/**
	 * @param projectTag
	 *            the projectTag to set
	 */
	public void setProjectTag(String projectTag) {
		this.projectTag = projectTag;
	}

	/**
	 * @return the ratioName
	 */
	public String getRatioName() {
		return ratioName;
	}

	/**
	 * @param ratioName
	 *            the ratioName to set
	 */
	public void setRatioName(String ratioName) {
		this.ratioName = ratioName;
	}

	/**
	 * @return the amountType
	 */
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
	 * @return the scoreName
	 */
	public String getScoreName() {
		return scoreName;
	}

	/**
	 * @param scoreName
	 *            the scoreName to set
	 */
	public void setScoreName(String scoreName) {
		this.scoreName = scoreName;
	}

	protected static int compareNumberStrings(String string1, String string2, boolean ascendant, boolean ignoreCase) {
		String tmp1 = string1;
		String tmp2 = string2;
		if ("".equals(string1) || string1 == null)
			tmp1 = "0";
		if ("".equals(string2) || string2 == null)
			tmp2 = "0";
		try {
			Double d1 = Double.valueOf(tmp1);
			Double d2 = Double.valueOf(tmp2);
			return d1.compareTo(d2);
		} catch (NumberFormatException e) {
			return compareStrings(tmp1, tmp2, ascendant, ignoreCase);
		}

	}

	/**
	 * @return the ascendant
	 */
	public boolean isAscendant() {
		return ascendant;
	}

	/**
	 * @param ascendant
	 *            the ascendant to set
	 */
	public void setAscendant(boolean ascendant) {
		this.ascendant = ascendant;
	}

	protected static int compareNumbers(Number o1, Number o2, boolean ascendant) {

		if (o1 == null && o2 != null) {
			return ascendant ? 1 : -1;
		}
		if (o1 != null && o2 == null) {
			return ascendant ? -1 : 1;
		}
		if (o1 == null && o2 == null) {
			return 0;
		}
		return Double.compare(o1.doubleValue(), o2.doubleValue());
	}

	/**
	 * Compares strings taking into account that they could be null, so we want
	 * that in that case, the null strings were at the end, which depends in the
	 * order, determined by ignoreCase.
	 *
	 * @param string1
	 * @param string2
	 * @param ascendant
	 * @param ignoreCase
	 * @return
	 */
	protected static int compareStrings(String string1, String string2, boolean ascendant, boolean ignoreCase) {
		if ("".equals(string1)) {
			string1 = null;
		}
		if ("".equals(string2)) {
			string2 = null;
		}
		if (string1 == null && string2 != null) {
			return ascendant ? 1 : -1;
		}
		if (string1 != null && string2 == null) {
			return ascendant ? -1 : 1;
		}
		if (string1 == null && string2 == null) {
			return 0;
		}
		if (ignoreCase) {
			return string1.compareToIgnoreCase(string2);
		} else {
			return string1.compareTo(string2);
		}
	}

	protected static int compareRatios(ContainsRatios o1, ContainsRatios o2, String condition1Name,
			String condition2Name, String projectTag, String ratioName, boolean skipInfinities, boolean ascendant) {
		final List<RatioBean> ratios1 = o1.getRatiosByConditions(condition1Name, condition2Name, projectTag, ratioName,
				skipInfinities);
		final List<RatioBean> ratios2 = o2.getRatiosByConditions(condition1Name, condition2Name, projectTag, ratioName,
				skipInfinities);
		if (!ratios1.isEmpty() && !ratios2.isEmpty()) {
			final List<Double> ratioValues1 = SharedDataUtils.getRatioValues(condition1Name, condition2Name, ratios1);
			final List<Double> ratioValues2 = SharedDataUtils.getRatioValues(condition1Name, condition2Name, ratios2);
			if (ratioValues1.size() == 1 && ratioValues2.size() == 1) {
				return Double.compare(ratioValues1.get(0), ratioValues2.get(0));
			} else {
				// TODO sort in a concrete way when having several ratios??
				final Double efectiveRatio1 = SharedDataUtils.getEfectiveRatio(ratioValues1);
				final Double efectiveRatio2 = SharedDataUtils.getEfectiveRatio(ratioValues2);
				if (efectiveRatio1 != null && efectiveRatio2 != null) {
					return Double.compare(efectiveRatio1, efectiveRatio2);
				}
			}
		} else if (ratios1.isEmpty() && !ratios2.isEmpty()) {
			return ascendant ? 1 : -1;
		} else if (!ratios1.isEmpty() && ratios2.isEmpty()) {
			return ascendant ? -1 : 1;
		}
		return 0;

	}

	protected static int compareRatioScores(ContainsRatios o1, ContainsRatios o2, String condition1Name,
			String condition2Name, String projectTag, String ratioName, boolean skipInfinities, boolean ascendant) {
		try {
			final List<RatioBean> ratios1 = o1.getRatiosByConditions(condition1Name, condition2Name, projectTag,
					ratioName, skipInfinities);
			final List<RatioBean> ratios2 = o2.getRatiosByConditions(condition1Name, condition2Name, projectTag,
					ratioName, skipInfinities);
			if (!ratios1.isEmpty() && !ratios2.isEmpty()) {
				final List<ScoreBean> ratioValues1 = SharedDataUtils.getRatioScoreValues(condition1Name, condition2Name,
						ratios1);
				final List<ScoreBean> ratioValues2 = SharedDataUtils.getRatioScoreValues(condition1Name, condition2Name,
						ratios2);

				final String value1 = ratioValues1.get(0).getValue();
				final String value2 = ratioValues2.get(0).getValue();
				try {
					return Double.compare(Double.valueOf(value1), Double.valueOf(value2));
				} catch (NumberFormatException e) {
					return value1.compareTo(value2);
				}

			} else if (ratios1.isEmpty() && !ratios2.isEmpty()) {
				return ascendant ? 1 : -1;
			} else if (!ratios1.isEmpty() && ratios2.isEmpty()) {
				return ascendant ? -1 : 1;
			}

		} catch (Exception e) {

		}
		return 0;
	}
}
