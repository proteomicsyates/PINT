package edu.scripps.yates.annotations.uniprot;

import edu.scripps.yates.annotations.uniprot.xml.DbReferenceType;
import edu.scripps.yates.annotations.uniprot.xml.Entry;
import edu.scripps.yates.annotations.uniprot.xml.EvidencedStringType;
import edu.scripps.yates.annotations.uniprot.xml.GeneNameType;
import edu.scripps.yates.annotations.uniprot.xml.GeneType;
import edu.scripps.yates.annotations.uniprot.xml.OrganismNameType;
import edu.scripps.yates.annotations.uniprot.xml.OrganismType;
import edu.scripps.yates.annotations.uniprot.xml.ProteinType;
import edu.scripps.yates.annotations.uniprot.xml.ProteinType.RecommendedName;
import edu.scripps.yates.annotations.uniprot.xml.SequenceType;
import edu.scripps.yates.utilities.fasta.FastaParser;
import edu.scripps.yates.utilities.pattern.Adapter;

public class UniprotEntryAdapterFromFASTA implements Adapter<Entry> {
	private static final String SCIENTIFIC = "scientific";
	private static final String NCBI_TAXONOMY = "NCBI Taxonomy";
	private static final String PRIMARY = "primary";

	private final String accession;
	private final String sequence;
	private final String description;
	private final String gene;
	private final String organism;
	private final String sequenceVersion;
	private final String organismNCBIID;
	private final String name;

	public UniprotEntryAdapterFromFASTA(String accession, String fastaHeader, String sequence) {
		this.accession = accession;
		this.sequence = sequence;
		description = FastaParser.getDescription(fastaHeader);
		gene = FastaParser.getGeneFromFastaHeader(fastaHeader);
		name = FastaParser.getUniProtProteinName(fastaHeader);
		organism = FastaParser.getOrganismNameFromFastaHeader(fastaHeader, null);
		organismNCBIID = FastaParser.getOrganismNCBIIDFromFastaHeader(fastaHeader);
		sequenceVersion = FastaParser.getSequenceVersionFromFastaHeader(fastaHeader);
	}

	@Override
	public Entry adapt() {
		final Entry ret = new Entry();
		if (accession != null) {
			ret.getAccession().add(accession);
		}
		try {
			ret.setVersion(Integer.valueOf(sequenceVersion));
		} catch (final NumberFormatException e) {
		}
		if (name != null) {
			ret.getName().add(name);
		}

		if (description != null) {
			final ProteinType protein = new ProteinType();
			final EvidencedStringType evidence = new EvidencedStringType();
			evidence.setValue(description);
			final RecommendedName recommendedName = new RecommendedName();
			recommendedName.setFullName(evidence);
			protein.setRecommendedName(recommendedName);
			ret.setProtein(protein);
		}
		if (gene != null) {
			final GeneType geneType = new GeneType();
			final GeneNameType geneNameType = new GeneNameType();
			geneNameType.setValue(gene);
			geneNameType.setType(PRIMARY);
			geneType.getName().add(geneNameType);
			ret.getGene().add(geneType);
		}
		if (sequence != null) {
			final SequenceType sequenceType = new SequenceType();
			sequenceType.setLength(sequence.length());
			sequenceType.setValue(sequence);
			try {
				sequenceType.setVersion(Integer.valueOf(sequenceVersion));
			} catch (final NumberFormatException e) {
			}
			ret.setSequence(sequenceType);
		}
		if (organism != null) {
			final OrganismType organism = new OrganismType();
			final OrganismNameType organismName = new OrganismNameType();
			organismName.setType(SCIENTIFIC);
			organismName.setValue(this.organism);
			organism.getName().add(organismName);
			ret.setOrganism(organism);
		}
		if (organismNCBIID != null) {
			if (ret.getOrganism() == null) {
				final OrganismType organism = new OrganismType();
				ret.setOrganism(organism);
			}
			final DbReferenceType ncbiTaxonomyReference = new DbReferenceType();
			ncbiTaxonomyReference.setType(NCBI_TAXONOMY);
			ncbiTaxonomyReference.setId(organismNCBIID);
			ret.getOrganism().getDbReference().add(ncbiTaxonomyReference);
		}
		return ret;
	}
}
