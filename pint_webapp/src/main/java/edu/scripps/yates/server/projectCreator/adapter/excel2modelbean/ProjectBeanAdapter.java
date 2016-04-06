package edu.scripps.yates.server.projectCreator.adapter.excel2modelbean;

import edu.scripps.yates.excel.proteindb.importcfg.jaxb.ExperimentalConditionType;
import edu.scripps.yates.excel.proteindb.importcfg.jaxb.ExperimentalDesignType;
import edu.scripps.yates.excel.proteindb.importcfg.jaxb.ProjectType;
import edu.scripps.yates.proteindb.persistence.mysql.adapter.Adapter;
import edu.scripps.yates.shared.model.ProjectBean;

public class ProjectBeanAdapter implements Adapter<ProjectBean> {

	private final ProjectType project;

	public ProjectBeanAdapter(ProjectType project) {
		this.project = project;
	}

	@Override
	public ProjectBean adapt() {
		ProjectBean ret = new ProjectBean();
		ret.setDescription(project.getDescription());
		ret.setTag(project.getTag());
		ret.setName(project.getName());
		ret.setReleaseDate(project.getReleaseDate().toGregorianCalendar()
				.getTime());
		ExperimentalDesignType experimentalDesign = project
				.getExperimentalDesign();
		if (project.getExperimentalConditions() != null) {
			for (ExperimentalConditionType experimentalConditionType : project
					.getExperimentalConditions().getExperimentalCondition()) {
				ret.getConditions().add(
						new ExperimentalConditionBeanAdapter(
								experimentalConditionType, experimentalDesign)
								.adapt());
			}
		}
		return ret;
	}
}
