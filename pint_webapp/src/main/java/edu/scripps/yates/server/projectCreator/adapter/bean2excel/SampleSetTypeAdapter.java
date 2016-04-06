package edu.scripps.yates.server.projectCreator.adapter.bean2excel;

import edu.scripps.yates.excel.proteindb.importcfg.jaxb.SampleSetType;
import edu.scripps.yates.proteindb.persistence.mysql.adapter.Adapter;
import edu.scripps.yates.shared.model.projectCreator.excel.SampleSetTypeBean;
import edu.scripps.yates.shared.model.projectCreator.excel.SampleTypeBean;

public class SampleSetTypeAdapter implements Adapter<SampleSetType> {
	private final SampleSetTypeBean sampleSet;

	public SampleSetTypeAdapter(SampleSetTypeBean sampleSet) {
		this.sampleSet = sampleSet;
	}

	@Override
	public SampleSetType adapt() {
		SampleSetType ret = new SampleSetType();
		if (sampleSet.getSample() != null) {
			for (SampleTypeBean sampleTypeBean : sampleSet.getSample()) {
				ret.getSample().add(
						new SampleTypeAdapter(sampleTypeBean).adapt());
			}
		}
		return ret;
	}

}
