package edu.scripps.yates.annotations.uniprot.variant.model;

import edu.scripps.yates.utilities.pattern.Adapter;
import uk.ac.ebi.kraken.interfaces.uniprot.features.VariantFeature;

public class VariantAdapterFromNaturalVariant implements Adapter<Variant> {
	private final VariantFeature varSeq;
	private final String wholeOriginalSeq;

	public VariantAdapterFromNaturalVariant(VariantFeature varSeq, String wholeOriginalSeq) {
		this.varSeq = varSeq;
		this.wholeOriginalSeq = wholeOriginalSeq;
	}

	@Override
	public Variant adapt() {
		String id = varSeq.getFeatureId().getValue();
		String seq = VariantsUtil.translateSequence(varSeq, wholeOriginalSeq);
		String description = VariantsUtil.getDescription(varSeq);
		Variant variant = new Variant(id, seq, description, VariantType.NATURAL_VARIANT);
		return variant;
	}

}
