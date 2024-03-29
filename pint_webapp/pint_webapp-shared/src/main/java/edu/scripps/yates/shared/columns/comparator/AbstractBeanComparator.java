package edu.scripps.yates.shared.columns.comparator;

import java.io.Serializable;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import edu.scripps.yates.shared.columns.ColumnName;
import edu.scripps.yates.shared.model.AmountType;
import edu.scripps.yates.shared.model.PeptideBean;
import edu.scripps.yates.shared.model.RatioBean;
import edu.scripps.yates.shared.model.ScoreBean;
import edu.scripps.yates.shared.model.interfaces.ContainsRatios;
import edu.scripps.yates.shared.util.Pair;
import edu.scripps.yates.shared.util.SharedDataUtil;

public abstract class AbstractBeanComparator<T> implements Comparator<T>, Serializable {
	/**
	 *
	 */
	private static final long serialVersionUID = 6993812445713528073L;
	protected ColumnName columnName;
	protected String conditionName;
	// in case of ratios
	protected String condition2Name;
	protected String projectTag;
	protected String ratioName;
	protected AmountType amountType;
	protected String scoreName;

	public AbstractBeanComparator() {
		columnName = null;
		conditionName = null;
		condition2Name = null;
		projectTag = null;
		ratioName = null;
		amountType = null;
		this.scoreName = null;
	};

	public AbstractBeanComparator(ColumnName columnName) {
		this.columnName = columnName;
		conditionName = null;
		condition2Name = null;
		projectTag = null;
		ratioName = null;
		amountType = null;
		scoreName = null;
	}

	public AbstractBeanComparator(ColumnName columnName, String experimentalConditionName, AmountType amountType,
			String projectTag) {
		this.columnName = columnName;
		conditionName = experimentalConditionName;
		condition2Name = null;
		this.projectTag = projectTag;
		ratioName = null;
		this.amountType = amountType;
		this.scoreName = null;

	}

	public AbstractBeanComparator(ColumnName columnName, String scoreName) {
		this.columnName = columnName;
		conditionName = null;
		condition2Name = null;
		this.projectTag = null;
		ratioName = null;
		this.amountType = null;
		this.scoreName = scoreName;
	}

	public AbstractBeanComparator(ColumnName columnName, String experimentalCondition1Name, String experimentalCondition2Name,
			String projectTag, String ratioName) {
		this.columnName = columnName;
		conditionName = experimentalCondition1Name;
		condition2Name = experimentalCondition2Name;
		this.projectTag = projectTag;
		this.ratioName = ratioName;
		amountType = null;
		this.scoreName = null;
	}

	public AbstractBeanComparator(ColumnName columnName, String experimentalCondition1Name, String experimentalCondition2Name,
			String projectTag, String ratioName, String ratioScoreName) {
		this.columnName = columnName;
		conditionName = experimentalCondition1Name;
		condition2Name = experimentalCondition2Name;
		this.projectTag = projectTag;
		this.ratioName = ratioName;
		amountType = null;
		this.scoreName = ratioScoreName;
	}

	/**
	 * @return the columnName
	 */
	public ColumnName getColumnName() {
		return columnName;
	}

	/**
	 * @param columnName the columnName to set
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
	 * @param conditionName the conditionName to set
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
	 * @param condition2Name the condition2Name to set
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
	 * @param projectTag the projectTag to set
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
	 * @param ratioName the ratioName to set
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
	 * @param amountType the amountType to set
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
	 * @param scoreName the scoreName to set
	 */
	public void setScoreName(String scoreName) {
		this.scoreName = scoreName;
	}

	protected static int compareNumberStrings(String string1, String string2, boolean ignoreCase) {
		final String tmp1 = string1;
		final String tmp2 = string2;
		if ("".equals(string1) || string1 == null) {
			return -1;
		}
		if ("".equals(string2) || string2 == null) {
			return 1;
		}
		try {
			final Double d1 = Double.valueOf(tmp1);
			final Double d2 = Double.valueOf(tmp2);
			return d1.compareTo(d2);
		} catch (final NumberFormatException e) {
			return compareStrings(tmp1, tmp2, ignoreCase);
		}

	}

	protected static int compareNumbers(Number o1, Number o2) {

		if (o1 == null && o2 != null) {
			return -1;
		}
		if (o1 != null && o2 == null) {
			return 1;
		}
		if (o1 == null && o2 == null) {
			return 0;
		}

		return compareDouble(o1.doubleValue(), o2.doubleValue());
	}

	private static int compareDouble(double d1, double d2) {
		Double double1 = d1;
		Double double2 = d2;
		if (Double.isNaN(d1)) {
			double1 = null;
		}
		if (Double.isNaN(d2)) {
			double2 = null;
		}
		if (double1 == null || double2 == null) {
			return compareNumbers(double1, double2);
		}
		return Double.compare(d1, d2);
	}

	/**
	 * Compares strings taking into account that they could be null, so we want that
	 * in that case, the null strings were at the end, which depends in the order,
	 * determined by ignoreCase.
	 *
	 * @param string1
	 * @param string2
	 * @param ascendant
	 * @param ignoreCase
	 * @return
	 */
	protected static int compareStrings(String string1, String string2, boolean ignoreCase) {
		if ("".equals(string1)) {
			string1 = null;
		}
		if ("".equals(string2)) {
			string2 = null;
		}
		if (string1 == null && string2 != null) {
			return 1;
		}
		if (string1 != null && string2 == null) {
			return -1;
		}
		if (string1 == null && string2 == null) {
			return 0;
		}
		if (ignoreCase) {
			final int ret1 = string1.compareToIgnoreCase(string2);
			return ret1;
		} else {
			final int ret2 = string1.compareTo(string2);
			return ret2;
		}
	}

	/**
	 * Compares strings taking into account that they could be null, so we want that
	 * in that case, the null strings were at the end, which depends in the order,
	 * determined by ignoreCase. The nonNullString1 and 2 are secondary parameters
	 * to sort by just in case the result is 0 by the default parameters
	 * 
	 * @param string1
	 * @param string2
	 * @param ascendant
	 * @param ignoreCase
	 * @param nonNullString1
	 * @param nonNullString2
	 * @return
	 */
	protected static int compareStrings(String string1, String string2, boolean ignoreCase, String nonNullString1,
			String nonNullString2) {
		if ("".equals(string1)) {
			string1 = null;
		}
		if ("".equals(string2)) {
			string2 = null;
		}
		if (string1 == null && string2 != null) {
			return 1;
		}
		if (string1 != null && string2 == null) {
			return -1;
		}
		if (string1 == null && string2 == null) {
			return 0;
		}
		if (ignoreCase) {
			final int ret1 = string1.compareToIgnoreCase(string2);
			if (ret1 == 0) {
				return nonNullString1.compareToIgnoreCase(nonNullString2);
			}
			return ret1;
		} else {
			final int ret2 = string1.compareTo(string2);
			if (ret2 == 0) {
				nonNullString1.compareTo(nonNullString2);
			}
			return ret2;
		}
	}

	protected static int compareRatios(ContainsRatios o1, ContainsRatios o2, String condition1Name,
			String condition2Name, String projectTag, String ratioName, boolean skipInfinities) {
		final List<RatioBean> ratios1 = o1.getRatiosByConditions(condition1Name, condition2Name, projectTag, ratioName,
				skipInfinities);
		final List<RatioBean> ratios2 = o2.getRatiosByConditions(condition1Name, condition2Name, projectTag, ratioName,
				skipInfinities);
		if (!ratios1.isEmpty() && !ratios2.isEmpty()) {
			final List<Double> ratioValues1 = SharedDataUtil.getRatioValues(condition1Name, condition2Name, ratios1);
			final List<Double> ratioValues2 = SharedDataUtil.getRatioValues(condition1Name, condition2Name, ratios2);
			if (ratioValues1.size() == 1 && ratioValues2.size() == 1) {
				return compareNumbers(ratioValues1.get(0), ratioValues2.get(0));
			} else {
				// TODO sort in a concrete way when having several ratios??
				final Double efectiveRatio1 = SharedDataUtil.getEfectiveRatio(ratioValues1);
				final Double efectiveRatio2 = SharedDataUtil.getEfectiveRatio(ratioValues2);
				if (efectiveRatio1 != null && efectiveRatio2 != null) {
					return compareNumbers(efectiveRatio1, efectiveRatio2);
				}
			}
		} else if (ratios1.isEmpty() && !ratios2.isEmpty()) {
			return 1;
		} else if (!ratios1.isEmpty() && ratios2.isEmpty()) {
			return -1;
		}
		return 0;

	}

	protected static int compareRatioScores(ContainsRatios o1, ContainsRatios o2, String condition1Name,
			String condition2Name, String projectTag, String ratioName, String ratioScoreName, boolean skipInfinities) {
		try {
			final List<RatioBean> ratios1 = o1.getRatiosByConditions(condition1Name, condition2Name, projectTag,
					ratioName, skipInfinities);
			final List<RatioBean> ratios2 = o2.getRatiosByConditions(condition1Name, condition2Name, projectTag,
					ratioName, skipInfinities);
			if (!ratios1.isEmpty() && !ratios2.isEmpty()) {
				final List<ScoreBean> ratioValues1 = SharedDataUtil.getRatioScoreValues(condition1Name, condition2Name,
						ratios1, ratioScoreName);
				final List<ScoreBean> ratioValues2 = SharedDataUtil.getRatioScoreValues(condition1Name, condition2Name,
						ratios2, ratioScoreName);
				String value1 = null;
				String value2 = null;
				for (final ScoreBean scoreBean : ratioValues1) {
					value1 = scoreBean.getValue();

				}

				for (final ScoreBean scoreBean : ratioValues2) {
					value2 = scoreBean.getValue();
				}
				try {
					return compareNumbers(Double.valueOf(value1), Double.valueOf(value2));
				} catch (final NumberFormatException e) {
					return value1.compareTo(value2);
				}

			} else if (ratios1.isEmpty() && !ratios2.isEmpty()) {
				return 1;
			} else if (!ratios1.isEmpty() && ratios2.isEmpty()) {
				return -1;
			}

		} catch (final Exception e) {

		}
		return 0;
	}

	protected static int getMinPosition(PeptideBean o1) {
		int min = Integer.MAX_VALUE;
		final Map<String, List<Pair<Integer, Integer>>> startingPositions = o1.getStartingPositions();
		for (final List<Pair<Integer, Integer>> positions : startingPositions.values()) {
			for (final Pair<Integer, Integer> startAndEnd : positions) {
				final int position = startAndEnd.getFirstElement();
				if (min > position)
					min = position;
			}
		}

		return min;
	}

	protected static int compareNumberStrings(String string1, String string2) {
		String tmp1 = string1;
		String tmp2 = string2;
		if ("".equals(string1) || string1 == null)
			tmp1 = "0";
		if ("".equals(string2) || string2 == null)
			tmp2 = "0";
		try {
			final Double d1 = Double.valueOf(tmp1);
			final Double d2 = Double.valueOf(tmp2);
			return d1.compareTo(d2);
		} catch (final NumberFormatException e) {
			return tmp1.compareTo(tmp2);
		}

	}
}
