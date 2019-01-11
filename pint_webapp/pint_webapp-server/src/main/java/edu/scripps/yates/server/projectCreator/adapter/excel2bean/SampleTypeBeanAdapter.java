package edu.scripps.yates.server.projectCreator.adapter.excel2bean;

import edu.scripps.yates.excel.proteindb.importcfg.jaxb.SampleType;
import edu.scripps.yates.proteindb.persistence.mysql.adapter.Adapter;
import edu.scripps.yates.shared.model.projectCreator.excel.SampleTypeBean;

public class SampleTypeBeanAdapter implements Adapter<SampleTypeBean> {
	private final SampleType sampleType;

	public SampleTypeBeanAdapter(SampleType sampleType) {
		this.sampleType = sampleType;
	}

	@Override
	public SampleTypeBean adapt() {

		return adaptFromSampleTypeBean();

	}

	private SampleTypeBean adaptFromSampleTypeBean() {
		SampleTypeBean ret = new SampleTypeBean();
		ret.setDescription(sampleType.getDescription());
		ret.setId(sampleType.getId());
		ret.setLabelRef(sampleType.getLabelRef());
		ret.setOrganismRef(sampleType.getOrganismRef());
		ret.setTissueRef(sampleType.getTissueRef());
		ret.setWt(sampleType.isWt());
		return ret;
	}

}
