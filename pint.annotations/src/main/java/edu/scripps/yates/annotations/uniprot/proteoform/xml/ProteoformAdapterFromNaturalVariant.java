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

	public ProteoformAdapterFromNaturalVariant(String originalACC, FeatureType feature, String wholeOriginalSeq) {
		this.varSeq = feature;
		this.wholeOriginalSeq = wholeOriginalSeq;
		this.originalACC = originalACC;
	}

	@Override
	public Proteoform adapt() {
		String id = originalACC + "_" + varSeq.getId();
		String seq = ProteoformUtil.translateSequence(varSeq, wholeOriginalSeq);
		String description = ProteoformUtil.getDescription(varSeq);
		Proteoform variant = new Proteoform(originalACC, id, seq, description, ProteoformType.NATURAL_VARIANT);
		return variant;
	}

}
