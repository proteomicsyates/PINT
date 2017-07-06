package edu.scripps.yates.excel.proteindb.importcfg.adapter;

import java.util.List;
import java.util.Map;

import edu.scripps.yates.excel.proteindb.importcfg.jaxb.IdDescriptionType;
import edu.scripps.yates.excel.proteindb.importcfg.jaxb.LabelSetType;
import edu.scripps.yates.excel.proteindb.importcfg.jaxb.LabelType;
import edu.scripps.yates.excel.proteindb.importcfg.jaxb.OrganismSetType;
import edu.scripps.yates.excel.proteindb.importcfg.jaxb.SampleType;
import edu.scripps.yates.excel.proteindb.importcfg.jaxb.TissueSetType;
import edu.scripps.yates.utilities.model.factories.SampleEx;
import edu.scripps.yates.utilities.proteomicsmodel.Sample;
import gnu.trove.map.hash.THashMap;

public class SampleAdapter implements edu.scripps.yates.utilities.pattern.Adapter<Sample> {

	private final SampleType sampleCfg;
	private final OrganismSetType organismSet;
	private final TissueSetType tissueSet;
	private final LabelSetType labelSetType;
	private static final Map<String, Sample> map = new THashMap<String, Sample>();

	public SampleAdapter(SampleType sampleCfg, OrganismSetType organismSet, TissueSetType tissueSet,
			LabelSetType labelSetType) {
		this.sampleCfg = sampleCfg;
		this.organismSet = organismSet;
		this.tissueSet = tissueSet;
		this.labelSetType = labelSetType;
	}

	@Override
	public Sample adapt() {
		if (map.containsKey(sampleCfg.getId()))
			return map.get(sampleCfg.getId());
		SampleEx sample = new SampleEx(sampleCfg.getId());
		map.put(sampleCfg.getId(), sample);
		sample.setDescription(sampleCfg.getDescription());

		if (sampleCfg.getLabelRef() != null) {
			LabelType labelCfg = getLabel(sampleCfg.getLabelRef());
			if (labelCfg != null)
				sample.setLabel(new LabelAdapter(labelCfg).adapt());
		}
		sample.setWildType(sampleCfg.isWt());
		if (sampleCfg.getOrganismRef() != null)
			sample.setOrganism(new OrganismAdapter(getOrganism(sampleCfg.getOrganismRef())).adapt());
		if (sampleCfg.getTissueRef() != null)
			sample.setTissue(new TissueAdapter(getTissue(sampleCfg.getTissueRef())).adapt());
		return sample;
	}

	private LabelType getLabel(String labelRef) {
		if (labelRef == null)
			return null;
		if (labelSetType != null && !labelSetType.getLabel().isEmpty()) {
			for (LabelType label : labelSetType.getLabel()) {
				if (label.getId().equals(labelRef))
					return label;
			}
		}
		throw new IllegalArgumentException("Referenced label '" + labelRef + "' is not found in the project");
	}

	private IdDescriptionType getOrganism(String organismRef) {
		if (organismRef == null)
			return null;
		final List<IdDescriptionType> organisms = organismSet.getOrganism();
		for (IdDescriptionType organism : organisms) {
			if (organism.getId().equals(organismRef))
				return organism;
		}
		throw new IllegalArgumentException("Referenced organism '" + organismRef + "' is not found in the project");
	}

	private IdDescriptionType getTissue(String tissueRef) {
		if (tissueRef == null)
			return null;
		final List<IdDescriptionType> tissues = tissueSet.getTissue();
		for (IdDescriptionType tissue : tissues) {
			if (tissue.getId().equals(tissueRef))
				return tissue;
		}
		throw new IllegalArgumentException("Referenced tissue '" + tissueRef + "' is not found in the project");
	}

	protected static void clearStaticInformation() {
		map.clear();
	}
}
