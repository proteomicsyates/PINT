package edu.scripps.yates.utilities.model.factories;

import java.io.Serializable;

import edu.scripps.yates.utilities.model.enums.AggregationLevel;
import edu.scripps.yates.utilities.model.enums.CombinationType;
import edu.scripps.yates.utilities.proteomicsmodel.Condition;
import edu.scripps.yates.utilities.proteomicsmodel.Protein;
import edu.scripps.yates.utilities.proteomicsmodel.Ratio;
import edu.scripps.yates.utilities.proteomicsmodel.Score;

/**
 * This class represents a ratio of the expression of a {@link Protein} between
 * two {@link ConditionEx}s
 *
 * @author Salva
 *
 */
public class RatioEx implements Ratio, Serializable {

	/**
	 *
	 */
	private static final long serialVersionUID = -4788750494296885167L;
	private final double value;
	private final Condition condition1;
	private final Condition condition2;
	private final String description;
	private Score score;
	private CombinationType combinationType;
	private final AggregationLevel aggregationLevel;

	public RatioEx(double value, Condition condition1, Condition condition2, String ratioDescription,
			AggregationLevel aggregationLevel) {
		super();
		this.value = value;
		this.condition1 = condition1;
		this.condition2 = condition2;
		description = ratioDescription;
		if (description == null) {
			System.out.println("asdf");
		}
		this.aggregationLevel = aggregationLevel;
	}

	public RatioEx(double value, Condition condition1, Condition condition2, CombinationType combinationType,
			String ratioDescription, AggregationLevel aggregationLevel) {
		super();
		this.value = value;
		this.condition1 = condition1;
		this.condition2 = condition2;
		description = ratioDescription;
		if (description == null) {
			System.out.println("asdf");
		}
		this.combinationType = combinationType;
		this.aggregationLevel = aggregationLevel;
	}

	/**
	 * @return the value
	 */
	@Override
	public double getValue() {
		return value;
	}

	/**
	 * @return the condition1
	 */
	@Override
	public Condition getCondition1() {
		return condition1;
	}

	/**
	 * @return the condition2
	 */
	@Override
	public Condition getCondition2() {
		return condition2;
	}

	/**
	 * @return the description
	 */
	@Override
	public String getDescription() {
		return description;
	}

	@Override
	public Score getAssociatedConfidenceScore() {
		return score;
	}

	public void setAssociatedConfidenceScore(Score score) {
		this.score = score;
	}

	@Override
	public CombinationType getCombinationType() {
		return combinationType;
	}

	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object arg0) {
		if (arg0 instanceof RatioEx) {
			RatioEx ratio = (RatioEx) arg0;
			if (ratio.getValue() != getValue())
				return false;
			if (!ratio.getCondition1().equals(getCondition1()))
				return false;
			if (!ratio.getCondition2().equals(getCondition2()))
				return false;
			if (ratio.getDescription() != null && getDescription() != null
					&& !ratio.getDescription().equals(getDescription()))
				return false;
			return true;
		}

		return super.equals(arg0);

	}

	@Override
	public AggregationLevel getAggregationLevel() {
		return aggregationLevel;
	}

	/**
	 * @return the score
	 */
	public Score getScore() {
		return score;
	}

	/**
	 * @param score
	 *            the score to set
	 */
	public void setScore(Score score) {
		this.score = score;
	}

	/**
	 * @param combinationType
	 *            the combinationType to set
	 */
	public void setCombinationType(CombinationType combinationType) {
		this.combinationType = combinationType;
	}

}
