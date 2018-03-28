package edu.scripps.yates.annotations.uniprot.variant.model;

import edu.scripps.yates.utilities.pattern.Adapter;
import uk.ac.ebi.kraken.interfaces.uniprot.features.MutagenFeature;

public class VariantAdapterFromMutagenFeature implements Adapter<Variant> {
	private final MutagenFeature feature;
	private final String wholeOriginalSeq;

	public VariantAdapterFromMutagenFeature(MutagenFeature varSeq, String wholeOriginalSeq) {
		this.feature = varSeq;
		this.wholeOriginalSeq = wholeOriginalSeq;
	}

	@Override
	public Variant adapt() {
		String id = "Mutagenesis in " + VariantsUtil.getLocationString(feature);
		String seq = VariantsUtil.translateSequence(feature, wholeOriginalSeq);
		String description = VariantsUtil.getDescription(feature);
		Variant variant = new Variant(id, seq, description, VariantType.NATURAL_VARIANT);
		return variant;
	}

}
