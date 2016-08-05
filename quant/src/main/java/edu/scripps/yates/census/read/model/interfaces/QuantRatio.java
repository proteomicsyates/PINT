package edu.scripps.yates.census.read.model.interfaces;

import edu.scripps.yates.census.analysis.QuantCondition;
import edu.scripps.yates.census.read.util.QuantificationLabel;

public interface QuantRatio extends edu.scripps.yates.utilities.proteomicsmodel.Ratio {

	public Double getLog2Ratio(QuantificationLabel labelNumerator, QuantificationLabel labelDenominator);

	public Double getNonLogRatio(QuantificationLabel labelNumerator, QuantificationLabel labelDenominator);

	public QuantificationLabel getLabel1();

	public QuantificationLabel getLabel2();

	public Double getLog2Ratio(QuantCondition quantConditionNumerator, QuantCondition quantConditionDenominator);

	public Double getNonLogRatio(QuantCondition quantConditionNumerator, QuantCondition quantConditionDenominator);

	public Double getLog2Ratio(String condition1Name, String condition2Name);

	public Double getNonLogRatio(String condition1Name, String condition2Name);

	public QuantCondition getQuantCondition1();

	public QuantCondition getQuantCondition2();

}
