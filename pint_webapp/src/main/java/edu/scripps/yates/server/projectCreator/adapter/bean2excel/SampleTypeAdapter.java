package edu.scripps.yates.server.projectCreator.adapter.bean2excel;

import edu.scripps.yates.excel.proteindb.importcfg.jaxb.SampleType;
import edu.scripps.yates.proteindb.persistence.mysql.adapter.Adapter;
import edu.scripps.yates.shared.model.SampleBean;
import edu.scripps.yates.shared.model.projectCreator.excel.SampleTypeBean;

public class SampleTypeAdapter implements Adapter<SampleType> {
	private final SampleBean sample;
	private final SampleTypeBean sampleTypeBean;

	public SampleTypeAdapter(SampleBean sample) {
		this.sample = sample;
		sampleTypeBean = null;
	}

	public SampleTypeAdapter(SampleTypeBean sampleTypeBean) {
		sample = null;
		this.sampleTypeBean = sampleTypeBean;
	}

	@Override
	public SampleType adapt() {
		if (sample != null) {
			return adaptFromSampleBean();
		} else if (sampleTypeBean != null) {
			return adaptFromSampleTypeBean();
		}
		return null;

	}

	private SampleType adaptFromSampleTypeBean() {
		SampleType ret = new SampleType();
		ret.setDescription(sampleTypeBean.getDescription());
		ret.setId(sampleTypeBean.getId());
		ret.setLabelRef(sampleTypeBean.getLabelRef());
		ret.setOrganismRef(sampleTypeBean.getOrganismRef());
		ret.setTissueRef(sampleTypeBean.getTissueRef());
		ret.setWt(sampleTypeBean.isWt());
		return ret;
	}

	private SampleType adaptFromSampleBean() {
		SampleType ret = new SampleType();
		ret.setDescription(sample.getDescription());
		ret.setId(sample.getId());
		if (sample.getLabel() != null)
			ret.setLabelRef(sample.getLabel().getId());
		if (sample.getOrganism() != null)
			ret.setOrganismRef(sample.getOrganism().getNcbiTaxID());
		if (sample.getTissue() != null)
			ret.setTissueRef(sample.getTissue().getTissueID());
		ret.setWt(sample.isWildType());
		return ret;
	}

}
