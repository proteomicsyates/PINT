package edu.scripps.yates.annotations.uniprot.variant.model;

import edu.scripps.yates.utilities.pattern.Adapter;
import uk.ac.ebi.kraken.interfaces.uniprot.features.ConflictFeature;

public class VariantAdapterFromConflictFeature implements Adapter<Variant> {
	private final ConflictFeature conflictFeature;
	private final String wholeOriginalSeq;

	public VariantAdapterFromConflictFeature(ConflictFeature conflictFeature, String wholeOriginalSeq) {
		this.conflictFeature = conflictFeature;
		this.wholeOriginalSeq = wholeOriginalSeq;
	}

	@Override
	public Variant adapt() {
		String id = "Conflict in " + VariantsUtil.getLocationString(conflictFeature);

		String seq = VariantsUtil.translateSequence(conflictFeature, wholeOriginalSeq);
		String description = VariantsUtil.getDescription(conflictFeature);
		Variant variant = new Variant(id, seq, description, VariantType.SEQUENCE_CONFLICT);
		return variant;
	}

}
