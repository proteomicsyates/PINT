package edu.scripps.yates.annotations.uniprot.proteoform.xml;

import edu.scripps.yates.annotations.uniprot.proteoform.Proteoform;
import edu.scripps.yates.annotations.uniprot.proteoform.ProteoformType;
import edu.scripps.yates.annotations.uniprot.proteoform.ProteoformUtil;
import edu.scripps.yates.annotations.uniprot.xml.FeatureType;
import edu.scripps.yates.utilities.pattern.Adapter;

public class ProteoformAdapterFromNaturalVariant implements Adapter<Proteoform> {
	private final FeatureType varSeq;
	private final String wholeOriginalSeq;
	private final String originalACC;
	private final String taxonomy;
	private final String gene;
	private final String originalDescription;

	public ProteoformAdapterFromNaturalVariant(String originalACC, String originalDescription, FeatureType feature,
			String wholeOriginalSeq, String gene, String taxonomy) {
		this.varSeq = feature;
		this.wholeOriginalSeq = wholeOriginalSeq;
		this.originalACC = originalACC;
		this.taxonomy = taxonomy;
		this.gene = gene;
		this.originalDescription = originalDescription;
	}

	@Override
	public Proteoform adapt() {
		String id = getID(originalACC, varSeq);
		String seq = ProteoformUtil.translateSequence(varSeq, wholeOriginalSeq);
		String description = ProteoformUtil.getDescription(varSeq, originalDescription);
		Proteoform variant = new Proteoform(originalACC, id, seq, description, gene, taxonomy,
				ProteoformType.NATURAL_VARIANT);
		return variant;
	}

	private String getID(String originalACC, FeatureType feature) {
		String ret = "";
		if (originalACC != null) {
			ret += originalACC;
		}
		if (feature.getId() != null) {
			ret += "_" + feature.getId();
		} else {
			ret += "_variant";
			if (feature.getVariation() != null && !feature.getVariation().isEmpty()) {
				ret += "_" + feature.getVariation().get(0);
			}
			ret += "_" + ProteoformUtil.getLocationString(feature);
		}
		return ret;
	}
}
