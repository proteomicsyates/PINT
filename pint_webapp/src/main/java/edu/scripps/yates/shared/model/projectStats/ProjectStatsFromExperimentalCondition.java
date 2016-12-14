package edu.scripps.yates.client.gui.components.projectItems.util;

import edu.scripps.yates.shared.model.ExperimentalConditionBean;

public class ProjectStatsFromExperimentalCondition extends ProjectStats<ExperimentalConditionBean> {

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

}
