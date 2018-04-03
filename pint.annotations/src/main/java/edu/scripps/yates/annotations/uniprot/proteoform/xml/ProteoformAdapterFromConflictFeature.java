package edu.scripps.yates.annotations.uniprot.proteoform.xml;

import edu.scripps.yates.annotations.uniprot.proteoform.Proteoform;
import edu.scripps.yates.annotations.uniprot.proteoform.ProteoformType;
import edu.scripps.yates.annotations.uniprot.proteoform.ProteoformUtil;
import edu.scripps.yates.annotations.uniprot.xml.FeatureType;
import edu.scripps.yates.utilities.pattern.Adapter;

public class ProteoformAdapterFromConflictFeature implements Adapter<Proteoform> {
	private final FeatureType conflictFeature;
	private final String wholeOriginalSeq;
	private final String originalACC;
	private final String taxonomy;
	private final String gene;
	private final String originalDescription;

	public ProteoformAdapterFromConflictFeature(String originalACC, String originalDescription,
			FeatureType conflictFeature, String wholeOriginalSeq, String gene, String taxonomy) {
		this.conflictFeature = conflictFeature;
		this.wholeOriginalSeq = wholeOriginalSeq;
		this.originalACC = originalACC;
		this.taxonomy = taxonomy;
		this.gene = gene;
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
