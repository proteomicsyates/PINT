package edu.scripps.yates.server.projectCreator.adapter.bean2excel;

import java.util.List;

import edu.scripps.yates.excel.proteindb.importcfg.jaxb.ExperimentalDesignType;
import edu.scripps.yates.excel.proteindb.importcfg.jaxb.LabelSetType;
import edu.scripps.yates.excel.proteindb.importcfg.jaxb.OrganismSetType;
import edu.scripps.yates.excel.proteindb.importcfg.jaxb.SampleSetType;
import edu.scripps.yates.excel.proteindb.importcfg.jaxb.TissueSetType;
import edu.scripps.yates.proteindb.persistence.mysql.adapter.Adapter;
import edu.scripps.yates.shared.model.LabelBean;
import edu.scripps.yates.shared.model.OrganismBean;
import edu.scripps.yates.shared.model.SampleBean;
import edu.scripps.yates.shared.model.TissueBean;
import edu.scripps.yates.shared.model.projectCreator.excel.ExperimentalDesignTypeBean;

public class ExperimentalDesignTypeAdapter implements
		Adapter<ExperimentalDesignType> {
	private final List<OrganismBean> organisms;
	private final List<TissueBean> tissues;
	private final List<LabelBean> labels;
	private final List<SampleBean> samples;
	private final ExperimentalDesignTypeBean experimentalDesignTypeBean;

	public ExperimentalDesignTypeAdapter(List<SampleBean> samples,
			List<OrganismBean> organisms, List<TissueBean> tissues,
			List<LabelBean> labels) {
		this.organisms = organisms;
		this.tissues = tissues;
		this.labels = labels;
		this.samples = samples;
		experimentalDesignTypeBean = null;
	}

	public ExperimentalDesignTypeAdapter(
			ExperimentalDesignTypeBean experimentalDesignTypeBean) {
		this.experimentalDesignTypeBean = experimentalDesignTypeBean;
		organisms = null;
		tissues = null;
		labels = null;
		samples = null;
	}

	@Override
	public ExperimentalDesignType adapt() {
		if (experimentalDesignTypeBean != null) {
			return adaptFromExperimentalDesignTypeBean();
		} else {
			return adaptFromOtherObjects();
		}
	}

	private ExperimentalDesignType adaptFromExperimentalDesignTypeBean() {
		ExperimentalDesignType ret = new ExperimentalDesignType();
		if (experimentalDesignTypeBean.getLabelSet() != null
				&& !experimentalDesignTypeBean.getLabelSet().getLabel()
						.isEmpty())
			ret.setLabelSet(new LabelSetTypeAdapter(experimentalDesignTypeBean
					.getLabelSet()).adapt());
		if (experimentalDesignTypeBean.getOrganismSet() != null)
			ret.setOrganismSet(new OrganismSetTypeAdapter(
					experimentalDesignTypeBean.getOrganismSet()).adapt());
		if (experimentalDesignTypeBean.getSampleSet() != null)
			ret.setSampleSet(new SampleSetTypeAdapter(
					experimentalDesignTypeBean.getSampleSet()).adapt());
		if (experimentalDesignTypeBean.getTissueSet() != null)
			ret.setTissueSet(new TissueSetTypeAdapter(
					experimentalDesignTypeBean.getTissueSet()).adapt());
		return ret;

	}

	private ExperimentalDesignType adaptFromOtherObjects() {
		ExperimentalDesignType ret = new ExperimentalDesignType();
		if (samples != null) {
			final SampleSetType sampleSetType = new SampleSetType();
			for (SampleBean sample : samples) {
				sampleSetType.getSample().add(
						new SampleTypeAdapter(sample).adapt());
			}
			ret.setSampleSet(sampleSetType);
		}
		if (organisms != null) {
			OrganismSetType organismSetType = new OrganismSetType();
			for (OrganismBean organism : organisms) {
				organismSetType.getOrganism().add(
						new IdDescriptionTypeAdapter(organism.getNcbiTaxID(),
								organism.getId()).adapt());
			}
			ret.setOrganismSet(organismSetType);
		}
		if (tissues != null) {
			TissueSetType tissueSetType = new TissueSetType();
			for (TissueBean tissue : tissues) {
				tissueSetType.getTissue().add(
						new IdDescriptionTypeAdapter(tissue.getTissueID(),
								tissue.getId()).adapt());
			}
			ret.setTissueSet(tissueSetType);
		}
		if (labels != null) {
			LabelSetType labelSetType = new LabelSetType();
			for (LabelBean label : labels) {
				labelSetType.getLabel()
						.add(new LabelTypeAdapter(label).adapt());
			}
			ret.setLabelSet(labelSetType);
		}
		return ret;
	}

}
