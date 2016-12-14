package edu.scripps.yates.shared.model.projectStats;

import edu.scripps.yates.shared.model.ExperimentalConditionBean;

public class ProjectStatsFromExperimentalCondition extends ProjectStats<ExperimentalConditionBean> {

	/**
	 *
	 */
	private static final long serialVersionUID = 1918926996245029304L;

	public ProjectStatsFromExperimentalCondition() {
		super();
	}

	public ProjectStatsFromExperimentalCondition(ExperimentalConditionBean t) {
		super(t);
	}

	@Override
	public Integer getNumSamples() {
		return t.getSample() != null ? 1 : 0;
	}

	@Override
	public Integer getNumConditions() {

		return 1;
	}

	@Override
	protected ProjectStatsType getType() {
		return ProjectStatsType.CONDITION;
	}

}
