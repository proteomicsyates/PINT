package edu.scripps.yates.server.projectCreator.adapter.excel2bean;

import edu.scripps.yates.excel.proteindb.importcfg.jaxb.ExperimentalConditionType;
import edu.scripps.yates.proteindb.persistence.mysql.adapter.Adapter;
import edu.scripps.yates.shared.model.projectCreator.excel.ExperimentalConditionTypeBean;

public class ExperimentalConditionTypeBeanAdapter implements
		Adapter<ExperimentalConditionTypeBean> {
	private final ExperimentalConditionType experimentalConditionType;

	public ExperimentalConditionTypeBeanAdapter(
			ExperimentalConditionType experimentalConditionTypeBean) {
		experimentalConditionType = experimentalConditionTypeBean;
	}

	@Override
	public ExperimentalConditionTypeBean adapt() {

		return adaptFromExperimentalConditionTypeBean();

	}

	private ExperimentalConditionTypeBean adaptFromExperimentalConditionTypeBean() {
		ExperimentalConditionTypeBean ret = new ExperimentalConditionTypeBean();
		ret.setDescription(experimentalConditionType.getDescription());
		ret.setId(experimentalConditionType.getId());
		if (experimentalConditionType.getIdentificationInfo() != null)
			ret.setIdentificationInfo(new IdentificationInfoTypeBeanAdapter(
					experimentalConditionType.getIdentificationInfo()).adapt());
		if (experimentalConditionType.getQuantificationInfo() != null)
			ret.setQuantificationInfo(new QuantificationInfoTypeBeanAdapter(
					experimentalConditionType.getQuantificationInfo()).adapt());
		ret.setSampleRef(experimentalConditionType.getSampleRef());
		return ret;
	}

}
