package edu.scripps.yates.shared.columns.comparator;

import java.io.Serializable;
import java.util.Comparator;

import edu.scripps.yates.shared.columns.ColumnName;
import edu.scripps.yates.shared.model.AmountType;

public abstract class BeanComparator<T> implements Comparator<T>, Serializable {
	ColumnName columnName;
	String conditionName;
	// in case of ratios
	String condition2Name;
	String projectTag;
	String ratioName;
	AmountType amountType;
	String scoreName;

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

	protected static int compareNumberStrings(String string1, String string2) {
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
			return tmp1.compareTo(tmp2);
		}

	}
	// public abstract String getDBColumnName();

}
