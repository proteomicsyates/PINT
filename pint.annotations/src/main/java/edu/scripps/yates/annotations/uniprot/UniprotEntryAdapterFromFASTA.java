package edu.scripps.yates.annotations.uniprot;

import edu.scripps.yates.annotations.uniprot.xml.Entry;
import edu.scripps.yates.annotations.uniprot.xml.EvidencedStringType;
import edu.scripps.yates.annotations.uniprot.xml.GeneNameType;
import edu.scripps.yates.annotations.uniprot.xml.GeneType;
import edu.scripps.yates.annotations.uniprot.xml.ProteinType;
import edu.scripps.yates.annotations.uniprot.xml.ProteinType.RecommendedName;
import edu.scripps.yates.annotations.uniprot.xml.SequenceType;
import edu.scripps.yates.utilities.fasta.FastaParser;
import edu.scripps.yates.utilities.pattern.Adapter;

public class UniprotEntryAdapterFromFASTA implements Adapter<Entry> {
	private final String accession;
	private final String sequence;
	private final String description;
	private final String gene;
	private final String organism;
	private final String sequenceVersion;

	public UniprotEntryAdapterFromFASTA(String accession, String fastaHeader, String sequence) {
		this.accession = accession;
		this.sequence = sequence;
		description = FastaParser.getDescription(fastaHeader);
		gene = FastaParser.getGeneFromFastaHeader(fastaHeader);
		organism = FastaParser.getOrganismNameFromFastaHeader(fastaHeader, accession);
		sequenceVersion = FastaParser.getSequenceVersionFromFastaHeader(fastaHeader);
	}

	@Override
	public Entry adapt() {
		Entry ret = new Entry();
		if (accession != null) {
			ret.getAccession().add(accession);
		}
		try {
			ret.setVersion(Integer.valueOf(sequenceVersion));
		} catch (NumberFormatException e) {
		}
		if (description != null) {
			ret.getName().add(description);
			final ProteinType protein = new ProteinType();
			final EvidencedStringType evidence = new EvidencedStringType();
			evidence.setValue(description);
			final RecommendedName recommendedName = new RecommendedName();
			recommendedName.setFullName(evidence);
			protein.setRecommendedName(recommendedName);
			ret.setProtein(protein);
		}
		if (gene != null) {
			GeneType geneType = new GeneType();
			GeneNameType geneNameType = new GeneNameType();
			geneNameType.setValue(gene);
			geneType.getName().add(geneNameType);
			ret.getGene().add(geneType);
		}
		if (sequence != null) {
			SequenceType sequenceType = new SequenceType();
			sequenceType.setLength(sequence.length());
			sequenceType.setValue(sequence);
			try {
				sequenceType.setVersion(Integer.valueOf(sequenceVersion));
			} catch (NumberFormatException e) {
			}
			ret.setSequence(sequenceType);
		}
		return ret;
	}
}
