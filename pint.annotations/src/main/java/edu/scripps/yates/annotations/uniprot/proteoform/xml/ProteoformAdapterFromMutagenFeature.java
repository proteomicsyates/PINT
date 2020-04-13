package edu.scripps.yates.annotations.uniprot.proteoform.xml;

import edu.scripps.yates.annotations.uniprot.proteoform.Proteoform;
import edu.scripps.yates.annotations.uniprot.proteoform.ProteoformType;
import edu.scripps.yates.annotations.uniprot.proteoform.ProteoformUtil;
import edu.scripps.yates.utilities.annotations.uniprot.xml.FeatureType;
import edu.scripps.yates.utilities.fasta.FastaParser;
import edu.scripps.yates.utilities.pattern.Adapter;

public class ProteoformAdapterFromMutagenFeature implements Adapter<Proteoform> {
	private final FeatureType feature;
	private final String wholeOriginalSeq;
	private final String originalACC;
	private final String taxonomy;
	private final String gene;
	private final String originalDescription;
	private final String name;
	private final String taxID;
	private final boolean isSwissprot;

	public ProteoformAdapterFromMutagenFeature(String originalACC, String name, String originalDescription,
			FeatureType varSeq, String wholeOriginalSeq, String gene, String taxonomy, String taxID,
			boolean isSwissProt) {
		feature = varSeq;
		this.wholeOriginalSeq = wholeOriginalSeq;
		this.originalACC = originalACC;
		this.name = name;
		this.taxonomy = taxonomy;
		this.gene = gene;
		this.originalDescription = originalDescription;
		this.taxID = taxID;
		this.isSwissprot = isSwissProt;
	}

	@Override
	public Proteoform adapt() {
		final String id = originalACC + FastaParser.mutated + ProteoformUtil.getShortDescription(feature);
		final String seq = ProteoformUtil.translateSequence(feature, wholeOriginalSeq);
		final String description = ProteoformUtil.getDescription(feature, originalDescription);
		final Proteoform variant = new Proteoform(originalACC, wholeOriginalSeq, id, seq, name, description, gene,
				taxonomy, taxID, ProteoformType.MUTAGENESIS_SITE, isSwissprot);
		return variant;
	}

}
