package edu.scripps.yates.annotations.uniprot.proteoform.japi;

import edu.scripps.yates.annotations.uniprot.proteoform.Proteoform;
import edu.scripps.yates.annotations.uniprot.proteoform.ProteoformType;
import edu.scripps.yates.annotations.uniprot.proteoform.ProteoformUtil;
import edu.scripps.yates.utilities.pattern.Adapter;
import uk.ac.ebi.kraken.interfaces.uniprot.features.ConflictFeature;

public class ProteoFormAdapterFromConflictFeature implements Adapter<Proteoform> {
	private final ConflictFeature conflictFeature;
	private final String wholeOriginalSeq;
	private final String originalACC;

	public ProteoFormAdapterFromConflictFeature(String originalACC, ConflictFeature conflictFeature,
			String wholeOriginalSeq) {
		this.conflictFeature = conflictFeature;
		this.wholeOriginalSeq = wholeOriginalSeq;
		this.originalACC = originalACC;
	}

	@Override
	public Proteoform adapt() {
		String id = originalACC + "_conflict_" + ProteoformUtil.getShortDescription(conflictFeature);

		String seq = ProteoformUtil.translateSequence(conflictFeature, wholeOriginalSeq);
		String description = ProteoformUtil.getDescription(conflictFeature);
		Proteoform variant = new Proteoform(originalACC, id, seq, description, ProteoformType.SEQUENCE_CONFLICT);
		return variant;
	}

}
