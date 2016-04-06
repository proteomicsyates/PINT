package edu.scripps.yates.server.projectCreator.adapter.bean2excel;

import edu.scripps.yates.excel.proteindb.importcfg.jaxb.ProjectType;
import edu.scripps.yates.proteindb.persistence.mysql.adapter.Adapter;
import edu.scripps.yates.shared.model.projectCreator.excel.ProjectTypeBean;
import edu.scripps.yates.utilities.dates.DatesUtil;

public class ProjectTypeAdapter implements Adapter<ProjectType> {

	private final ProjectTypeBean projectTypeBean;

	public ProjectTypeAdapter(ProjectTypeBean projectTypeBean) {
		this.projectTypeBean = projectTypeBean;

	}

	@Override
	public ProjectType adapt() {

		ProjectType ret = new ProjectType();
		ret.setDescription(projectTypeBean.getDescription());
		if (projectTypeBean.getExperimentalConditions() != null)
			ret.setExperimentalConditions(new ExperimentalConditionsTypeAdapter(
					projectTypeBean.getExperimentalConditions()).adapt());
		if (projectTypeBean.getExperimentalDesign() != null)
			ret.setExperimentalDesign(new ExperimentalDesignTypeAdapter(
					projectTypeBean.getExperimentalDesign()).adapt());
		if (projectTypeBean.getMsRuns() != null)
			ret.setMsRuns(new MsRunsTypeAdapter(projectTypeBean.getMsRuns())
					.adapt());
		ret.setName(projectTypeBean.getName());
		ret.setTag(projectTypeBean.getTag());
		ret.setQuantitative(projectTypeBean.isQuantitative());
		ret.setRatios(new RatiosTypeAdapter(projectTypeBean.getRatios())
				.adapt());
		if (projectTypeBean.getReleaseDate() != null) {
			ret.setReleaseDate(DatesUtil.toXMLGregorianCalendar(projectTypeBean
					.getReleaseDate()));
		}
		return ret;
	}

}
