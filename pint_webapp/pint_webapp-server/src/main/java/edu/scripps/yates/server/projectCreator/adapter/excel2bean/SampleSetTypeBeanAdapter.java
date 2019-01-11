package edu.scripps.yates.server.projectCreator.adapter.excel2bean;

import edu.scripps.yates.excel.proteindb.importcfg.jaxb.SampleSetType;
import edu.scripps.yates.excel.proteindb.importcfg.jaxb.SampleType;
import edu.scripps.yates.proteindb.persistence.mysql.adapter.Adapter;
import edu.scripps.yates.shared.model.projectCreator.excel.SampleSetTypeBean;

public class SampleSetTypeBeanAdapter implements Adapter<SampleSetTypeBean> {
	private final SampleSetType sampleSet;

	public SampleSetTypeBeanAdapter(SampleSetType sampleSet) {
		this.sampleSet = sampleSet;
	}

	@Override
	public SampleSetTypeBean adapt() {
		SampleSetTypeBean ret = new SampleSetTypeBean();
		if (sampleSet.getSample() != null) {
			for (SampleType sampleType : sampleSet.getSample()) {
				ret.getSample().add(
						new SampleTypeBeanAdapter(sampleType).adapt());
			}
		}
		return ret;
	}

}
