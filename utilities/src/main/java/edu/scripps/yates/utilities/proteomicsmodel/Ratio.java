package edu.scripps.yates.utilities.proteomicsmodel;

import edu.scripps.yates.utilities.model.enums.AggregationLevel;
import edu.scripps.yates.utilities.model.enums.CombinationType;

public interface Ratio {

	public double getValue();

	public Condition getCondition1();

	public Condition getCondition2();

	public String getDescription();

	public Score getAssociatedConfidenceScore();

	public CombinationType getCombinationType();

	public AggregationLevel getAggregationLevel();
}
