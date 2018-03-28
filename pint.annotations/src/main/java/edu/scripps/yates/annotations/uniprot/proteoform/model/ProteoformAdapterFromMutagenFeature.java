package edu.scripps.yates.annotations.uniprot.proteoform.model;

import edu.scripps.yates.utilities.pattern.Adapter;
import uk.ac.ebi.kraken.interfaces.uniprot.features.MutagenFeature;

public class ProteoformAdapterFromMutagenFeature implements Adapter<Proteoform> {
	private final MutagenFeature feature;
	private final String wholeOriginalSeq;

	public ProteoformAdapterFromMutagenFeature(MutagenFeature varSeq, String wholeOriginalSeq) {
		this.feature = varSeq;
		this.wholeOriginalSeq = wholeOriginalSeq;
	}

	@Override
	public Proteoform adapt() {
		String id = "Mutagenesis in " + ProteoformUtil.getLocationString(feature);
		String seq = ProteoformUtil.translateSequence(feature, wholeOriginalSeq);
		String description = ProteoformUtil.getDescription(feature);
		Proteoform variant = new Proteoform(id, seq, description, ProteoformType.NATURAL_VARIANT);
		return variant;
	}

}
