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

	public ProteoformAdapterFromMutagenFeature(String originalACC, FeatureType varSeq, String wholeOriginalSeq) {
		this.feature = varSeq;
		this.wholeOriginalSeq = wholeOriginalSeq;
		this.originalACC = originalACC;
	}

	@Override
	public Proteoform adapt() {
		String id = originalACC + "_mutated_" + ProteoformUtil.getShortDescription(feature);
		String seq = ProteoformUtil.translateSequence(feature, wholeOriginalSeq);
		String description = ProteoformUtil.getDescription(feature);
		Proteoform variant = new Proteoform(originalACC, id, seq, description, ProteoformType.NATURAL_VARIANT);
		return variant;
	}

}
