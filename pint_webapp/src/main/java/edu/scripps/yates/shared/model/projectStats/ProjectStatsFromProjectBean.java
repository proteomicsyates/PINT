package edu.scripps.yates.shared.model.projectStats;

import java.util.HashSet;
import java.util.Set;

import edu.scripps.yates.shared.model.ExperimentalConditionBean;
import edu.scripps.yates.shared.model.ProjectBean;
import edu.scripps.yates.shared.model.SampleBean;

public class ProjectStatsFromProjectBean extends ProjectStats<ProjectBean> {

	/**
	 *
	 */
	private static final long serialVersionUID = -4589953089543296124L;

	public ProjectStatsFromProjectBean() {
		super();
	}

	public ProjectStatsFromProjectBean(ProjectBean t) {
		super(t);
	}

	@Override
	public Integer getNumSamples() {
		Set<SampleBean> samples = new HashSet<SampleBean>();
		if (t != null && t.getConditions() != null) {
			for (ExperimentalConditionBean condition : t.getConditions()) {
				if (condition.getSample() != null) {
					samples.add(condition.getSample());
				}
			}
		}
		return samples.size();
	}

	@Override
	public Integer getNumConditions() {

		Set<ExperimentalConditionBean> conditions = new HashSet<ExperimentalConditionBean>();
		if (t != null && t.getConditions() != null) {
			conditions.addAll(t.getConditions());
		}
		return conditions.size();
	}

	@Override
	protected ProjectStatsType getType() {
		return ProjectStatsType.PROJECT;
	}

}
