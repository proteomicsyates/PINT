package edu.scripps.yates.server.projectCreator.adapter.excel2bean;

import edu.scripps.yates.excel.proteindb.importcfg.jaxb.ExperimentalDesignType;
import edu.scripps.yates.proteindb.persistence.mysql.adapter.Adapter;
import edu.scripps.yates.shared.model.projectCreator.excel.ExperimentalDesignTypeBean;

public class ExperimentalDesignTypeBeanAdapter implements
		Adapter<ExperimentalDesignTypeBean> {

	private final ExperimentalDesignType experimentalDesignType;

	public ExperimentalDesignTypeBeanAdapter(
			ExperimentalDesignType experimentalDesignType) {
		this.experimentalDesignType = experimentalDesignType;

	}

	@Override
	public ExperimentalDesignTypeBean adapt() {

		return adaptFromExperimentalDesignTypeBean();

	}

	private ExperimentalDesignTypeBean adaptFromExperimentalDesignTypeBean() {
		ExperimentalDesignTypeBean ret = new ExperimentalDesignTypeBean();
		if (experimentalDesignType.getLabelSet() != null)
			ret.setLabelSet(new LabelSetTypeBeanAdapter(experimentalDesignType
					.getLabelSet()).adapt());
		if (experimentalDesignType.getOrganismSet() != null)
			ret.setOrganismSet(new OrganismSetTypeBeanAdapter(
					experimentalDesignType.getOrganismSet()).adapt());
		if (experimentalDesignType.getSampleSet() != null)
			ret.setSampleSet(new SampleSetTypeBeanAdapter(experimentalDesignType
					.getSampleSet()).adapt());
		if (experimentalDesignType.getTissueSet() != null)
			ret.setTissueSet(new TissueSetTypeBeanAdapter(experimentalDesignType
					.getTissueSet()).adapt());
		return ret;

	}

}
