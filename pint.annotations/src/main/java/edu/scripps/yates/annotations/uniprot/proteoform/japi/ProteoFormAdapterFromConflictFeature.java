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
	private final String gene;
	private final String taxonomy;
	private final String originalDescription;

	public ProteoFormAdapterFromConflictFeature(String originalACC, String originalDescription,
			ConflictFeature conflictFeature, String wholeOriginalSeq, String gene, String taxonomy) {
		this.conflictFeature = conflictFeature;
		this.wholeOriginalSeq = wholeOriginalSeq;
		this.originalACC = originalACC;
		this.gene = gene;
		this.taxonomy = taxonomy;
		this.originalDescription = originalDescription;
	}

	@Override
	public Proteoform adapt() {
		String id = originalACC + "_conflict_" + ProteoformUtil.getShortDescription(conflictFeature);

		String seq = ProteoformUtil.translateSequence(conflictFeature, wholeOriginalSeq);
		String description = ProteoformUtil.getDescription(conflictFeature, originalDescription);
		Proteoform variant = new Proteoform(originalACC, id, seq, description, gene, taxonomy,
				ProteoformType.SEQUENCE_CONFLICT);
		return variant;
	}

}
