package edu.scripps.yates.shared.model.projectStats;

import edu.scripps.yates.shared.model.SampleBean;

public class ProjectStatsFromSample extends ProjectStats<SampleBean> {

	/**
	 *
	 */
	private static final long serialVersionUID = 8693063202346110654L;

	public ProjectStatsFromSample() {
		super();
	}

	public ProjectStatsFromSample(SampleBean t) {
		super(t);
	}

	@Override
	public Integer getNumSamples() {
		return 1;
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * edu.scripps.yates.client.gui.components.projectItems.util.ProjectStats#
	 * getNumConditions()
	 */
	@Override
	public Integer getNumConditions() {
		return 1;
	}

	@Override
	protected ProjectStatsType getType() {
		return ProjectStatsType.SAMPLE;
	}

}
