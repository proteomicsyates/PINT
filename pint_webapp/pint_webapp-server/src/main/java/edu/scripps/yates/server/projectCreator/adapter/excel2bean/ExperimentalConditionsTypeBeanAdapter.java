package edu.scripps.yates.server.projectCreator.adapter.excel2bean;

import edu.scripps.yates.excel.proteindb.importcfg.jaxb.ExperimentalConditionType;
import edu.scripps.yates.excel.proteindb.importcfg.jaxb.ExperimentalConditionsType;
import edu.scripps.yates.proteindb.persistence.mysql.adapter.Adapter;
import edu.scripps.yates.shared.model.projectCreator.excel.ExperimentalConditionsTypeBean;

public class ExperimentalConditionsTypeBeanAdapter implements
		Adapter<ExperimentalConditionsTypeBean> {
	private final ExperimentalConditionsType experimentalConditions;

	public ExperimentalConditionsTypeBeanAdapter(
			ExperimentalConditionsType experimentalConditions) {
		this.experimentalConditions = experimentalConditions;
	}

	@Override
	public ExperimentalConditionsTypeBean adapt() {
		ExperimentalConditionsTypeBean ret = new ExperimentalConditionsTypeBean();
		if (experimentalConditions.getExperimentalCondition() != null) {
			for (ExperimentalConditionType experimentalConditionTypeBean : experimentalConditions
					.getExperimentalCondition()) {
				ret.getExperimentalCondition().add(
						new ExperimentalConditionTypeBeanAdapter(
								experimentalConditionTypeBean).adapt());
			}
		}
		return ret;
	}
}
