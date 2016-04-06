package edu.scripps.yates.server.projectCreator.adapter.excel2modelbean;

import edu.scripps.yates.excel.proteindb.importcfg.jaxb.ExperimentalConditionType;
import edu.scripps.yates.excel.proteindb.importcfg.jaxb.ExperimentalDesignType;
import edu.scripps.yates.excel.proteindb.importcfg.jaxb.IdDescriptionType;
import edu.scripps.yates.excel.proteindb.importcfg.jaxb.OrganismSetType;
import edu.scripps.yates.excel.proteindb.importcfg.jaxb.SampleType;
import edu.scripps.yates.excel.proteindb.importcfg.jaxb.TissueSetType;
import edu.scripps.yates.excel.proteindb.importcfg.util.ImportCfgUtil;
import edu.scripps.yates.proteindb.persistence.mysql.adapter.Adapter;
import edu.scripps.yates.shared.model.ExperimentalConditionBean;

public class ExperimentalConditionBeanAdapter implements
		Adapter<ExperimentalConditionBean> {
	private final ExperimentalConditionType conditionType;
	private final ExperimentalDesignType experimentalDesignType;

	public ExperimentalConditionBeanAdapter(
			ExperimentalConditionType conditionType,
			ExperimentalDesignType experimentalDesignType) {
		this.conditionType = conditionType;
		this.experimentalDesignType = experimentalDesignType;
	}

	@Override
	public ExperimentalConditionBean adapt() {

		ExperimentalConditionBean conditionBean = new ExperimentalConditionBean();
		conditionBean.setName(conditionType.getId());
		conditionBean.setDescription(conditionType.getDescription());

		SampleType sampleType = ImportCfgUtil.getReferencedSample(
				conditionType.getSampleRef(), experimentalDesignType);

		if (sampleType != null) {
			OrganismSetType organismSetType = null;
			if (experimentalDesignType != null)
				organismSetType = experimentalDesignType.getOrganismSet();
			IdDescriptionType organismType = ImportCfgUtil
					.getReferencedOrganism(sampleType.getOrganismRef(),
							organismSetType);
			TissueSetType tissueSetType = null;
			if (experimentalDesignType != null)
				tissueSetType = experimentalDesignType.getTissueSet();
			IdDescriptionType tissueType = ImportCfgUtil.getReferencedTissue(
					sampleType.getTissueRef(), tissueSetType);
			conditionBean.setSample(new SampleBeanAdapter(sampleType,
					organismType, tissueType, experimentalDesignType).adapt());
		}

		return conditionBean;
	}

}
