package edu.scripps.yates.annotations.uniprot.proteoform.model;

import edu.scripps.yates.utilities.pattern.Adapter;
import uk.ac.ebi.kraken.interfaces.uniprot.features.VariantFeature;

public class ProteoformAdapterFromNaturalVariant implements Adapter<Proteoform> {
	private final VariantFeature varSeq;
	private final String wholeOriginalSeq;
	private final String originalACC;

	public ProteoformAdapterFromNaturalVariant(String originalACC, VariantFeature varSeq, String wholeOriginalSeq) {
		this.varSeq = varSeq;
		this.wholeOriginalSeq = wholeOriginalSeq;
		this.originalACC = originalACC;
	}

	@Override
	public Proteoform adapt() {
		String id = varSeq.getFeatureId().getValue();
		String seq = ProteoformUtil.translateSequence(varSeq, wholeOriginalSeq);
		String description = ProteoformUtil.getDescription(varSeq);
		Proteoform variant = new Proteoform(originalACC, id, seq, description, ProteoformType.NATURAL_VARIANT);
		return variant;
	}

}
