package edu.scripps.yates.census.read.model.interfaces;

import java.util.Set;

import edu.scripps.yates.census.analysis.QuantCondition;

public interface HasRatios {

	public Set<QuantRatio> getRatios();

	public QuantRatio getConsensusRatio(QuantCondition quantConditionNumerator,
			QuantCondition quantConditionDenominator);

	public QuantRatio getConsensusRatio(QuantCondition quantConditionNumerator,
			QuantCondition quantConditionDenominator, String replicateName);

	public Set<QuantRatio> getNonInfinityRatios();

	public void addRatio(QuantRatio ratio);

	public double getMeanRatios(QuantCondition quantConditionNumerator, QuantCondition quantConditionDenominator);

	public double getSTDRatios(QuantCondition quantConditionNumerator, QuantCondition quantConditionDenominator);

}
