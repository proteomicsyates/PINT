package edu.scripps.yates.census.read.model;

import java.util.Map;

import edu.scripps.yates.census.analysis.QuantCondition;
import edu.scripps.yates.census.read.model.interfaces.QuantRatio;
import edu.scripps.yates.census.read.util.QuantificationLabel;
import edu.scripps.yates.utilities.model.enums.AggregationLevel;
import edu.scripps.yates.utilities.model.enums.CombinationType;
import edu.scripps.yates.utilities.proteomicsmodel.Condition;
import edu.scripps.yates.utilities.proteomicsmodel.Score;

public class CensusRatio implements QuantRatio {
	private final QuantCondition quantConditionNumerator;
	private final QuantCondition quantConditionDenominator;
	private final QuantificationLabel labelNumerator;
	private final QuantificationLabel labelDenominator;
	private Double nonLogValue;
	private RatioScore ratioScore;
	private Double log2Value;
	private String description;
	private final AggregationLevel aggregationLevel;
	private CombinationType combinationType;

	public CensusRatio(QuantCondition quantConditionNumerator, QuantCondition quantConditionDenominator,
			AggregationLevel aggregationLevel, String description) {
		this(null, null, null, quantConditionNumerator, quantConditionDenominator, null, null, aggregationLevel,
				description);
	}

	public CensusRatio(Double ratioValue, String stdValue, Boolean isLogValue,
			Map<QuantificationLabel, QuantCondition> conditionsByLabels, QuantificationLabel labelNumerator,
			QuantificationLabel labelDenominator, AggregationLevel aggregationLevel, String description) {
		this(ratioValue, stdValue, isLogValue, conditionsByLabels.get(labelNumerator),
				conditionsByLabels.get(labelDenominator), labelNumerator, labelDenominator, aggregationLevel,
				description);
	}

	public CensusRatio(Double ratioValue, Boolean isLogValue,
			Map<QuantificationLabel, QuantCondition> conditionsByLabels, QuantificationLabel labelNumerator,
			QuantificationLabel labelDenominator, AggregationLevel aggregationLevel, String description) {
		this(ratioValue, null, isLogValue, conditionsByLabels.get(labelNumerator),
				conditionsByLabels.get(labelDenominator), labelNumerator, labelDenominator, aggregationLevel,
				description);
	}

	public CensusRatio(Double ratioValue, String stdValue, Boolean isLogValue, QuantCondition quantConditionNumerator,
			QuantCondition quantConditionDenominator, AggregationLevel aggregationLevel, String description) {
		this(ratioValue, stdValue, isLogValue, quantConditionNumerator, quantConditionDenominator, null, null,
				aggregationLevel, description);
	}

	public CensusRatio(Double ratioValue, Boolean isLogValue, QuantCondition quantConditionNumerator,
			QuantCondition quantConditionDenominator, AggregationLevel aggregationLevel, String description) {
		this(ratioValue, null, isLogValue, quantConditionNumerator, quantConditionDenominator, null, null,
				aggregationLevel, description);
	}

	public CensusRatio(Double ratioValue, String stdValue, Boolean isLogValue, QuantCondition quantConditionNumerator,
			QuantCondition quantConditionDenominator, QuantificationLabel labelNumerator,
			QuantificationLabel labelDenominator, AggregationLevel aggregationLevel, String description) {
		this.aggregationLevel = aggregationLevel;
		this.quantConditionNumerator = quantConditionNumerator;
		this.quantConditionDenominator = quantConditionDenominator;
		this.labelNumerator = labelNumerator;
		this.labelDenominator = labelDenominator;
		this.description = description;
		if (isLogValue != null && isLogValue && ratioValue != null) {
			nonLogValue = Math.pow(2, ratioValue);
		} else {
			nonLogValue = ratioValue;
		}
		if (stdValue != null) {
			ratioScore = new RatioScore(stdValue, "Standard deviation of ratios", "Standard deviation", null);
		}
	}

	private boolean containsLabels() {
		return labelNumerator != null && labelDenominator != null && labelDenominator != labelNumerator;
	}

	@Override
	public Double getLog2Ratio(QuantificationLabel labelNumerator, QuantificationLabel labelDenominator) {
		if (!containsLabels()) {
			throw new IllegalArgumentException("This ratio doesn't contains labels");
		}
		if (this.labelNumerator == labelNumerator && this.labelDenominator == labelDenominator) {
			return getLog2Value();
		} else if (this.labelNumerator == labelDenominator && this.labelDenominator == labelNumerator) {
			return -getLog2Value();
		} else {
			throw new IllegalArgumentException(
					"Labels are not used in this ratio. Use: " + this.labelNumerator + " and " + this.labelDenominator);
		}
	}

	private double getLog2Value() {
		if (log2Value == null) {
			log2Value = Math.log(nonLogValue) / Math.log(2);
		}
		return log2Value;
	}

	@Override
	public Double getNonLogRatio(QuantificationLabel labelNumerator, QuantificationLabel labelDenominator) {
		if (!containsLabels()) {
			throw new IllegalArgumentException("This ratio doesn't contains labels");
		}
		if (this.labelNumerator == labelNumerator && this.labelDenominator == labelDenominator) {
			return nonLogValue;
		} else if (this.labelNumerator == labelDenominator && this.labelDenominator == labelNumerator) {
			return 1 / nonLogValue;
		} else {
			throw new IllegalArgumentException(
					"Labels are not used in this ratio. Use: " + this.labelNumerator + " and " + this.labelDenominator);
		}
	}

	@Override
	public QuantificationLabel getLabel1() {
		return labelNumerator;
	}

	@Override
	public QuantificationLabel getLabel2() {
		return labelDenominator;
	}

	@Override
	public String getDescription() {
		return description;
	}

	/**
	 * @param description
	 *            the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	@Override
	public Double getLog2Ratio(QuantCondition quantConditionNumerator, QuantCondition quantConditionDenominator) {

		if (this.quantConditionNumerator.equals(quantConditionNumerator)
				&& this.quantConditionDenominator.equals(quantConditionDenominator)) {
			return getLog2Value();
		} else if (this.quantConditionNumerator.equals(quantConditionDenominator)
				&& this.quantConditionDenominator.equals(quantConditionNumerator)) {
			return -getLog2Value();
		} else {
			throw new IllegalArgumentException(
					"There is no ratio between these two conditions ('" + quantConditionNumerator.getName() + "','"
							+ quantConditionDenominator.getName() + "'). Use these ones: ('"
							+ this.quantConditionNumerator + "','" + quantConditionDenominator + "')");
		}
	}

	@Override
	public Double getNonLogRatio(QuantCondition quantConditionNumerator, QuantCondition quantConditionDenominator) {
		if (this.quantConditionNumerator.equals(quantConditionNumerator)
				&& this.quantConditionDenominator.equals(quantConditionDenominator)) {
			return nonLogValue;
		} else if (this.quantConditionNumerator.equals(quantConditionDenominator)
				&& this.quantConditionDenominator.equals(quantConditionNumerator)) {
			return 1 / nonLogValue;
		} else {
			throw new IllegalArgumentException(
					"Labels are not used in this ratio. Use: " + labelNumerator + " and " + labelDenominator);
		}
	}

	@Override
	public Double getLog2Ratio(String condition1Name, String condition2Name) {
		return getLog2Ratio(getConditionByName(condition1Name), getConditionByName(condition2Name));
	}

	private QuantCondition getConditionByName(String conditionName) {
		if (quantConditionNumerator.getName().equals(conditionName)) {
			return quantConditionNumerator;
		} else if (quantConditionDenominator.getName().equals(conditionName)) {
			return quantConditionDenominator;
		}
		return null;
	}

	@Override
	public Double getNonLogRatio(String condition1Name, String condition2Name) {
		return getNonLogRatio(getConditionByName(condition1Name), getConditionByName(condition2Name));
	}

	@Override
	public AggregationLevel getAggregationLevel() {
		return aggregationLevel;
	}

	public void setRatioScore(RatioScore ratioScore) {
		this.ratioScore = ratioScore;

	}

	@Override
	public double getValue() {
		return this.getNonLogRatio(quantConditionNumerator, quantConditionDenominator);
	}

	@Override
	public Condition getCondition1() {
		return quantConditionNumerator;
	}

	@Override
	public Condition getCondition2() {
		return quantConditionDenominator;
	}

	@Override
	public Score getAssociatedConfidenceScore() {
		return ratioScore;
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

	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof CensusRatio) {
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
		return "CensusRatio [quantConditionNumerator=" + quantConditionNumerator + ", quantConditionDenominator="
				+ quantConditionDenominator + ", labelNumerator=" + labelNumerator + ", labelDenominator="
				+ labelDenominator + ", nonLogValue=" + getNonLogRatio(labelNumerator, labelDenominator)
				+ ", ratioScore=" + ratioScore + ", log2Value=" + getLog2Ratio(labelNumerator, labelDenominator)
				+ ", description=" + description + ", aggregationLevel=" + aggregationLevel + ", combinationType="
				+ combinationType + "]";
	}

	@Override
	public QuantCondition getQuantCondition1() {
		return quantConditionNumerator;
	}

	@Override
	public QuantCondition getQuantCondition2() {
		return quantConditionDenominator;
	}

}
