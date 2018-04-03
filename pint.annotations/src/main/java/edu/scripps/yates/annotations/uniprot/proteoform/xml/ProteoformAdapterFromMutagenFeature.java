package edu.scripps.yates.annotations.uniprot.proteoform.xml;

import edu.scripps.yates.annotations.uniprot.proteoform.Proteoform;
import edu.scripps.yates.annotations.uniprot.proteoform.ProteoformType;
import edu.scripps.yates.annotations.uniprot.proteoform.ProteoformUtil;
import edu.scripps.yates.annotations.uniprot.xml.FeatureType;
import edu.scripps.yates.utilities.pattern.Adapter;

public class ProteoformAdapterFromMutagenFeature implements Adapter<Proteoform> {
	private final FeatureType feature;
	private final String wholeOriginalSeq;
	private final String originalACC;
	private final String taxonomy;
	private final String gene;
	private final String originalDescription;

	public ProteoformAdapterFromMutagenFeature(String originalACC, String originalDescription, FeatureType varSeq,
			String wholeOriginalSeq, String gene, String taxonomy) {
		this.feature = varSeq;
		this.wholeOriginalSeq = wholeOriginalSeq;
		this.originalACC = originalACC;
		this.taxonomy = taxonomy;
		this.gene = gene;
		this.originalDescription = originalDescription;
	}

	@Override
	public Proteoform adapt() {
		String id = originalACC + "_mutated_" + ProteoformUtil.getShortDescription(feature);
		String seq = ProteoformUtil.translateSequence(feature, wholeOriginalSeq);
		String description = ProteoformUtil.getDescription(feature, originalDescription);
		Proteoform variant = new Proteoform(originalACC, id, seq, description, gene, taxonomy,
				ProteoformType.NATURAL_VARIANT);
		return variant;
	}

}
