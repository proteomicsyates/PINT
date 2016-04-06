package edu.scripps.yates.server.projectCreator.adapter.excel2modelbean;

import edu.scripps.yates.excel.proteindb.importcfg.jaxb.ExperimentalDesignType;
import edu.scripps.yates.excel.proteindb.importcfg.jaxb.IdDescriptionType;
import edu.scripps.yates.excel.proteindb.importcfg.jaxb.LabelType;
import edu.scripps.yates.excel.proteindb.importcfg.jaxb.SampleType;
import edu.scripps.yates.excel.proteindb.importcfg.util.ImportCfgUtil;
import edu.scripps.yates.proteindb.persistence.mysql.adapter.Adapter;
import edu.scripps.yates.shared.model.SampleBean;

public class SampleBeanAdapter implements Adapter<SampleBean> {

	private final SampleType sampleType;
	private final IdDescriptionType organismType;
	private final IdDescriptionType tissueType;
	private final ExperimentalDesignType experimentalDesign;

	public SampleBeanAdapter(SampleType sampleType,
			IdDescriptionType organismType, IdDescriptionType tissueType,
			ExperimentalDesignType experimentalDesign) {
		this.sampleType = sampleType;
		this.organismType = organismType;
		this.tissueType = tissueType;
		this.experimentalDesign = experimentalDesign;
	}

	@Override
	public SampleBean adapt() {
		SampleBean sampleBean = new SampleBean();
		sampleBean.setName(sampleType.getId());
		sampleBean.setDescription(sampleType.getDescription());
		if (sampleType.getLabelRef() != null) {
			final LabelType referencedLabel = ImportCfgUtil.getReferencedLabel(
					sampleType.getLabelRef(), experimentalDesign);
			sampleBean.setLabel(new LabelBeanAdapter(referencedLabel).adapt());
		}
		if (organismType != null) {
			sampleBean.setOrganism(new OrganismBeanTypeAdapter(organismType)
					.adapt());
		}
		if (tissueType != null) {
			sampleBean.setTissue(new TissueBeanAdapter(tissueType).adapt());
		}
		return sampleBean;
	}
}
