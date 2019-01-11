package edu.scripps.yates.server.projectCreator.adapter.bean2excel;

import edu.scripps.yates.excel.proteindb.importcfg.jaxb.ExperimentalConditionsType;
import edu.scripps.yates.proteindb.persistence.mysql.adapter.Adapter;
import edu.scripps.yates.shared.model.projectCreator.excel.ExperimentalConditionTypeBean;
import edu.scripps.yates.shared.model.projectCreator.excel.ExperimentalConditionsTypeBean;

public class ExperimentalConditionsTypeAdapter implements
		Adapter<ExperimentalConditionsType> {
	private final ExperimentalConditionsTypeBean experimentalConditions;

	public ExperimentalConditionsTypeAdapter(
			ExperimentalConditionsTypeBean experimentalConditions) {
		this.experimentalConditions = experimentalConditions;
	}

	@Override
	public ExperimentalConditionsType adapt() {
		ExperimentalConditionsType ret = new ExperimentalConditionsType();
		if (experimentalConditions.getExperimentalCondition() != null) {
			for (ExperimentalConditionTypeBean experimentalConditionTypeBean : experimentalConditions
					.getExperimentalCondition()) {
				ret.getExperimentalCondition().add(
						new ExperimentalConditionTypeAdapter(
								experimentalConditionTypeBean).adapt());
			}
		}
		return ret;
	}
}
