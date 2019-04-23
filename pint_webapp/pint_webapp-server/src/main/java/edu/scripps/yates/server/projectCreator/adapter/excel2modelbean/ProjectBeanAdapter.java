package edu.scripps.yates.server.projectCreator.adapter.excel2modelbean;

import java.util.ArrayList;
import java.util.List;

import edu.scripps.yates.excel.proteindb.importcfg.jaxb.ExperimentalConditionType;
import edu.scripps.yates.excel.proteindb.importcfg.jaxb.ExperimentalDesignType;
import edu.scripps.yates.excel.proteindb.importcfg.jaxb.MsRunType;
import edu.scripps.yates.excel.proteindb.importcfg.jaxb.MsRunsType;
import edu.scripps.yates.excel.proteindb.importcfg.jaxb.ProjectType;
import edu.scripps.yates.excel.proteindb.importcfg.util.ImportCfgUtil;
import edu.scripps.yates.proteindb.persistence.mysql.adapter.Adapter;
import edu.scripps.yates.server.projectCreator.adapter.excel2bean.PrincipalInvestigatorBeanAdapter;
import edu.scripps.yates.shared.model.ProjectBean;

public class ProjectBeanAdapter implements Adapter<ProjectBean> {

	private final ProjectType project;

	public ProjectBeanAdapter(ProjectType project) {
		this.project = project;
	}

	@Override
	public ProjectBean adapt() {
		final ProjectBean ret = new ProjectBean();
		ret.setDescription(project.getDescription());
		ret.setTag(project.getTag());
		ret.setName(project.getName());
		ret.setReleaseDate(project.getReleaseDate().toGregorianCalendar().getTime());
		final ExperimentalDesignType experimentalDesign = project.getExperimentalDesign();
		if (project.getExperimentalConditions() != null) {
			for (final ExperimentalConditionType experimentalConditionType : project.getExperimentalConditions()
					.getExperimentalCondition()) {
				ret.getConditions()
						.add(new ExperimentalConditionBeanAdapter(experimentalConditionType, experimentalDesign, ret)
								.adapt());
			}
		}
		if (project.getPrincipalInvestigator() != null) {
			ret.setPrincipalInvestigator(
					new PrincipalInvestigatorBeanAdapter(project.getPrincipalInvestigator()).adapt());
		}
		if (project.getInstruments() != null) {
			final List<String> instruments = new ArrayList<String>();
			if (project.getInstruments().contains(ImportCfgUtil.PI_SEPARATOR)) {
				final String[] split = project.getInstruments().split(ImportCfgUtil.PI_SEPARATOR);
				for (final String string : split) {
					instruments.add(string);
				}
			} else {
				instruments.add(project.getInstruments());
			}
			ret.setInstruments(instruments);
		}
		final MsRunsType msRunsType = project.getMsRuns();
		if (msRunsType != null) {
			final List<MsRunType> msRuns = msRunsType.getMsRun();
			for (final MsRunType msRunType : msRuns) {
				ret.getMsRuns().add(new MSRunBeanAdapter(msRunType, ret).adapt());
			}
		}
		return ret;
	}
}
