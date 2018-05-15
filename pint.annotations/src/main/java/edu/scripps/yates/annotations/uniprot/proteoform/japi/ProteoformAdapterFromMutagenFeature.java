package edu.scripps.yates.annotations.uniprot.proteoform.japi;

import edu.scripps.yates.annotations.uniprot.proteoform.Proteoform;
import edu.scripps.yates.annotations.uniprot.proteoform.ProteoformType;
import edu.scripps.yates.annotations.uniprot.proteoform.ProteoformUtil;
import edu.scripps.yates.utilities.fasta.FastaParser;
import edu.scripps.yates.utilities.pattern.Adapter;
import uk.ac.ebi.kraken.interfaces.uniprot.features.MutagenFeature;

public class ProteoformAdapterFromMutagenFeature implements Adapter<Proteoform> {
	private final MutagenFeature feature;
	private final String wholeOriginalSeq;
	private final String originalACC;
	private final String gene;
	private final String taxonomy;
	private final String originalDescription;
	private final String name;

	public ProteoformAdapterFromMutagenFeature(String originalACC, String name, String originalDescription,
			MutagenFeature varSeq, String wholeOriginalSeq, String gene, String taxonomy) {
		feature = varSeq;
		this.wholeOriginalSeq = wholeOriginalSeq;
		this.originalACC = originalACC;
		this.gene = gene;
		this.name = name;
		this.taxonomy = taxonomy;
		this.originalDescription = originalDescription;
	}

	@Override
	public Proteoform adapt() {
		final String id = originalACC + FastaParser.mutated + ProteoformUtil.getShortDescription(feature);
		final String seq = ProteoformUtil.translateSequence(feature, wholeOriginalSeq);
		final String description = ProteoformUtil.getDescription(feature, originalDescription);
		final Proteoform variant = new Proteoform(originalACC, wholeOriginalSeq, id, seq, name, description, gene,
				taxonomy, ProteoformType.MUTAGENESIS_SITE);
		return variant;
	}

}
