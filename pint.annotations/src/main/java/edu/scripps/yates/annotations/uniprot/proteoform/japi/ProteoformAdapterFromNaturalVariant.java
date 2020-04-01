package edu.scripps.yates.annotations.uniprot.proteoform.japi;

import edu.scripps.yates.annotations.uniprot.proteoform.Proteoform;
import edu.scripps.yates.annotations.uniprot.proteoform.ProteoformType;
import edu.scripps.yates.annotations.uniprot.proteoform.ProteoformUtil;
import edu.scripps.yates.utilities.fasta.FastaParser;
import edu.scripps.yates.utilities.pattern.Adapter;
import uk.ac.ebi.kraken.interfaces.uniprot.features.VariantFeature;

public class ProteoformAdapterFromNaturalVariant implements Adapter<Proteoform> {
	private final VariantFeature varSeq;
	private final String wholeOriginalSeq;
	private final String originalACC;
	private final String gene;
	private final String taxonomy;
	private final String originalDescription;
	private final String name;
	private final String taxID;
	private final boolean isSwissprot;

	public ProteoformAdapterFromNaturalVariant(String originalACC, String name, String originalDescription,
			VariantFeature varSeq, String wholeOriginalSeq, String taxonomy, String gene, String taxID,
			boolean isSwissprot) {
		this.varSeq = varSeq;
		this.wholeOriginalSeq = wholeOriginalSeq;
		this.originalACC = originalACC;
		this.name = name;
		this.taxonomy = taxonomy;
		this.gene = gene;
		this.originalDescription = originalDescription;
		this.taxID = taxID;
		this.isSwissprot = isSwissprot;
	}

	@Override
	public Proteoform adapt() {
		final String featureID = varSeq.getFeatureId().getValue();

		String id = originalACC + FastaParser.variant;

		if (featureID != null) {
			id += featureID;
		}
		final String seq = ProteoformUtil.translateSequence(varSeq, wholeOriginalSeq);
		final String description = ProteoformUtil.getDescription(varSeq, originalDescription);
		final Proteoform variant = new Proteoform(originalACC, wholeOriginalSeq, id, seq, name, description, gene,
				taxonomy, taxID, ProteoformType.NATURAL_VARIANT, isSwissprot);
		return variant;
	}

}
