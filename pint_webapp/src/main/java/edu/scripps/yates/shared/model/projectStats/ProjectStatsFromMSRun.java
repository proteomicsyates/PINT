package edu.scripps.yates.shared.model.projectStats;

import edu.scripps.yates.shared.model.MSRunBean;

public class ProjectStatsFromMSRun extends ProjectStats<MSRunBean> {

	/**
	 *
	 */
	private static final long serialVersionUID = 2618273401218708237L;

	public ProjectStatsFromMSRun() {
		super();
	}

	public ProjectStatsFromMSRun(MSRunBean t) {
		super(t);
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * edu.scripps.yates.client.gui.components.projectItems.util.ProjectStats#
	 * getNumMSRuns()
	 */
	@Override
	public Integer getNumMSRuns() {
		return 1;
	}

	@Override
	protected ProjectStatsType getType() {
		return ProjectStatsType.MSRUN;
	}

}
