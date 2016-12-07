package edu.scripps.yates.client.gui.components.projectItems.util;

import edu.scripps.yates.shared.model.MSRunBean;

public class ProjectStatsFromMSRun extends ProjectStats<MSRunBean> {

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

}
