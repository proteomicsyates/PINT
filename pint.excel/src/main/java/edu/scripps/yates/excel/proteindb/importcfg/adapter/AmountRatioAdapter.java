package edu.scripps.yates.excel.proteindb.importcfg.adapter;

import edu.scripps.yates.utilities.proteomicsmodel.Condition;
import edu.scripps.yates.utilities.proteomicsmodel.Project;
import edu.scripps.yates.utilities.proteomicsmodel.Sample;
import edu.scripps.yates.utilities.proteomicsmodel.enums.AggregationLevel;
import edu.scripps.yates.utilities.proteomicsmodel.factories.RatioEx;

public class AmountRatioAdapter implements edu.scripps.yates.utilities.pattern.Adapter<RatioEx> {
	private final String ratioName;
	private final double ratioValue;
	private final Condition condition1;
	private final Condition condition2;
	private final AggregationLevel aggregationLevel;

	public AmountRatioAdapter(String ratioName, double ratioValue, String condition1Id, String condition2Id,
			Sample sample1, Sample sample2, Project project, AggregationLevel aggregationLevel) {
		this.ratioName = ratioName;
		this.ratioValue = ratioValue;
		this.aggregationLevel = aggregationLevel;
		condition1 = ConditionAdapter.getConditionById(project.getTag(), condition1Id);
		condition2 = ConditionAdapter.getConditionById(project.getTag(), condition2Id);

		// condition1 = ConditionAdapter.getExperimentalCondition(condition1Id,
		// sample1, project);
		// condition2 = ConditionAdapter.getExperimentalCondition(condition2Id,
		// sample2, project);

	}

	@Override
	public RatioEx adapt() {
		RatioEx proteinRatio = new RatioEx(ratioValue, condition1, condition2, ratioName, aggregationLevel);
		return proteinRatio;
	}

}
