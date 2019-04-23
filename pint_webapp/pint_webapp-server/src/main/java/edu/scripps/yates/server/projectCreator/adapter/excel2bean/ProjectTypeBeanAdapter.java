package edu.scripps.yates.server.projectCreator.adapter.excel2bean;

import java.util.ArrayList;
import java.util.List;

import edu.scripps.yates.excel.proteindb.importcfg.jaxb.ProjectType;
import edu.scripps.yates.excel.proteindb.importcfg.util.ImportCfgUtil;
import edu.scripps.yates.proteindb.persistence.mysql.adapter.Adapter;
import edu.scripps.yates.shared.model.projectCreator.excel.ProjectTypeBean;
import edu.scripps.yates.utilities.dates.DatesUtil;

public class ProjectTypeBeanAdapter implements Adapter<ProjectTypeBean> {

	private final ProjectType projectType;

	public ProjectTypeBeanAdapter(ProjectType projectType) {
		this.projectType = projectType;

	}

	@Override
	public ProjectTypeBean adapt() {
		return adaptFromProjectTypeBean();

	}

	private ProjectTypeBean adaptFromProjectTypeBean() {
		final ProjectTypeBean ret = new ProjectTypeBean();
		ret.setDescription(projectType.getDescription());
		if (projectType.getExperimentalConditions() != null) {
			ret.setExperimentalConditions(
					new ExperimentalConditionsTypeBeanAdapter(projectType.getExperimentalConditions()).adapt());
		}
		if (projectType.getExperimentalDesign() != null) {
			ret.setExperimentalDesign(
					new ExperimentalDesignTypeBeanAdapter(projectType.getExperimentalDesign()).adapt());
		}
		if (projectType.getMsRuns() != null) {
			ret.setMsRuns(new MsRunsTypeBeanAdapter(projectType.getMsRuns()).adapt());
		}
		if (projectType.getPrincipalInvestigator() != null) {
			ret.setPrincipalInvestigator(
					new PrincipalInvestigatorBeanAdapter(projectType.getPrincipalInvestigator()).adapt());
		}
		ret.setName(projectType.getName());
		ret.setTag(projectType.getTag());
		ret.setQuantitative(projectType.isQuantitative());
		if (projectType.getRatios() != null) {
			ret.setRatios(new RatiosTypeBeanAdapter(projectType.getRatios()).adapt());
		}
		if (projectType.getReleaseDate() != null) {
			ret.setReleaseDate(DatesUtil.toDate(projectType.getReleaseDate()));
		}
		if (projectType.getInstruments() != null) {
			final List<String> instruments = new ArrayList<String>();
			if (projectType.getInstruments().contains(ImportCfgUtil.PI_SEPARATOR)) {
				final String[] split = projectType.getInstruments().split(ImportCfgUtil.PI_SEPARATOR);
				for (final String string : split) {
					instruments.add(string);
				}
			} else {
				instruments.add(projectType.getInstruments());
			}
			ret.setInstruments(instruments);
		}
		return ret;
	}

}
