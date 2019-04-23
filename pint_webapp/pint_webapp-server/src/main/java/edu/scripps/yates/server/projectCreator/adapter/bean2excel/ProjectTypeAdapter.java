package edu.scripps.yates.server.projectCreator.adapter.bean2excel;

import edu.scripps.yates.excel.proteindb.importcfg.jaxb.ProjectType;
import edu.scripps.yates.excel.proteindb.importcfg.util.ImportCfgUtil;
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

		final ProjectType ret = new ProjectType();
		ret.setDescription(projectTypeBean.getDescription());
		if (projectTypeBean.getExperimentalConditions() != null)
			ret.setExperimentalConditions(
					new ExperimentalConditionsTypeAdapter(projectTypeBean.getExperimentalConditions()).adapt());
		if (projectTypeBean.getExperimentalDesign() != null)
			ret.setExperimentalDesign(
					new ExperimentalDesignTypeAdapter(projectTypeBean.getExperimentalDesign()).adapt());
		if (projectTypeBean.getMsRuns() != null)
			ret.setMsRuns(new MsRunsTypeAdapter(projectTypeBean.getMsRuns()).adapt());
		ret.setName(projectTypeBean.getName());
		ret.setTag(projectTypeBean.getTag());
		ret.setQuantitative(projectTypeBean.isQuantitative());
		if (projectTypeBean.getRatios() != null) {
			ret.setRatios(new RatiosTypeAdapter(projectTypeBean.getRatios()).adapt());
		}
		if (projectTypeBean.getReleaseDate() != null) {
			ret.setReleaseDate(DatesUtil.toXMLGregorianCalendar(projectTypeBean.getReleaseDate()));
		}
		if (projectTypeBean.getPrincipalInvestigator() != null) {
			ret.setPrincipalInvestigator(
					new PrincipalInvestigatorTypeAdapter(projectTypeBean.getPrincipalInvestigator()).adapt());
		}
		if (projectTypeBean.getInstruments() != null) {
			final StringBuilder sb = new StringBuilder();
			for (final String instrument : projectTypeBean.getInstruments()) {
				if (!"".equals(sb.toString())) {
					sb.append(ImportCfgUtil.PI_SEPARATOR);
				}
				sb.append(instrument);
			}
			ret.setInstruments(sb.toString());
		}
		return ret;
	}

}
