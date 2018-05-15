package edu.scripps.yates.annotations.uniprot.proteoform.xml;

import edu.scripps.yates.annotations.uniprot.proteoform.Proteoform;
import edu.scripps.yates.annotations.uniprot.proteoform.ProteoformType;
import edu.scripps.yates.annotations.uniprot.proteoform.ProteoformUtil;
import edu.scripps.yates.annotations.uniprot.xml.FeatureType;
import edu.scripps.yates.utilities.fasta.FastaParser;
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
		varSeq = feature;
		this.wholeOriginalSeq = wholeOriginalSeq;
		this.originalACC = originalACC;
		this.taxonomy = taxonomy;
		this.gene = gene;
		this.originalDescription = originalDescription;
	}

	@Override
	public Proteoform adapt() {
		final String id = getID(originalACC, varSeq);
		final String seq = ProteoformUtil.translateSequence(varSeq, wholeOriginalSeq);
		final String description = ProteoformUtil.getDescription(varSeq, originalDescription);
		final Proteoform variant = new Proteoform(originalACC, wholeOriginalSeq, id, seq, description, gene, taxonomy,
				ProteoformType.NATURAL_VARIANT);
		return variant;
	}

	private String getID(String originalACC, FeatureType feature) {
		final StringBuilder ret = new StringBuilder();
		if (originalACC != null) {
			ret.append(originalACC);
		}

		if (feature.getId() != null) {
			if (!"".equals(ret.toString())) {
				ret.append("_");
			}
			ret.append(feature.getId());
			ret.append("_").append(ProteoformUtil.getShortDescription(feature));
		} else {
			ret.append(FastaParser.variant);
			if (feature.getVariation() != null && !feature.getVariation().isEmpty()) {
				ret.append(feature.getVariation().get(0)).append("_").append(ProteoformUtil.getLocationString(feature));
			} else {
				ret.append(ProteoformUtil.getLocationString(feature));
			}
		}
		return ret.toString();
	}
}
