package edu.scripps.yates.census.read.model.interfaces;

import java.util.Set;

import edu.scripps.yates.census.analysis.QuantCondition;

public interface HasRatios {

	public Set<Ratio> getRatios();

	public double getMeanRatios(QuantCondition quantConditionNumerator, QuantCondition quantConditionDenominator);

	public double getSTDRatios(QuantCondition quantConditionNumerator, QuantCondition quantConditionDenominator);

}
