package edu.scripps.yates.client.gui.components.projectItems.util;

import edu.scripps.yates.shared.model.SampleBean;

public class ProjectStatsFromSample extends ProjectStats<SampleBean> {

	public ProjectStatsFromSample(SampleBean t) {
		super(t);
	}

	@Override
	public Integer getNumSamples() {
		return 1;
	}

	/* (non-Javadoc)
	 * @see edu.scripps.yates.client.gui.components.projectItems.util.ProjectStats#getNumConditions()
	 */
	@Override
	public Integer getNumConditions() {
		return 1;
	}

}
