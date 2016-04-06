package edu.scripps.yates.server.projectCreator.adapter.bean2excel;

import edu.scripps.yates.excel.proteindb.importcfg.jaxb.ExperimentalConditionType;
import edu.scripps.yates.proteindb.persistence.mysql.adapter.Adapter;
import edu.scripps.yates.shared.model.projectCreator.excel.ExperimentalConditionTypeBean;

public class ExperimentalConditionTypeAdapter implements
		Adapter<ExperimentalConditionType> {
	private final ExperimentalConditionTypeBean experimentalConditionTypeBean;

	public ExperimentalConditionTypeAdapter(
			ExperimentalConditionTypeBean experimentalConditionTypeBean) {
		this.experimentalConditionTypeBean = experimentalConditionTypeBean;
	}

	@Override
	public ExperimentalConditionType adapt() {

		ExperimentalConditionType ret = new ExperimentalConditionType();
		ret.setDescription(experimentalConditionTypeBean.getDescription());
		ret.setId(experimentalConditionTypeBean.getId());
		if (experimentalConditionTypeBean.getIdentificationInfo() != null) {
			ret.setIdentificationInfo(new IdentificationInfoTypeAdapter(
					experimentalConditionTypeBean.getIdentificationInfo())
					.adapt());
		}
		if (experimentalConditionTypeBean.getQuantificationInfo() != null) {
			ret.setQuantificationInfo(new QuantificationInfoTypeAdapter(
					experimentalConditionTypeBean.getQuantificationInfo())
					.adapt());
		}
		ret.setSampleRef(experimentalConditionTypeBean.getSampleRef());
		return ret;
	}

}
