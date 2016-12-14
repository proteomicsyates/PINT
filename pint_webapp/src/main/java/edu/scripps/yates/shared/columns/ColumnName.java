package edu.scripps.yates.shared.columns;

import edu.scripps.yates.shared.util.HorizontalAlignmentSharedConstant;

public enum ColumnName {
	ACC("Protein accession", "ACC", "Protein primary accession"),
	//
	DESCRIPTION("Protein description", "Desc.", "Protein recommended name"),
	//
	ALTERNATIVE_NAMES("Protein alternative names", "Alt. names", "Alternative names annotated for the protein"),
	//
	SEQUENCE_COUNT("Distinct peptide sequences", "Seq #", "Number of different peptide sequences",
			HorizontalAlignmentSharedConstant.ALIGN_CENTER),
	//
	SPECTRUM_COUNT("Spectrum count", "Spec #", "Number of spectrums", HorizontalAlignmentSharedConstant.ALIGN_CENTER),
	//
	COVERAGE("Protein sequence coverage", "Cov (%)", "Sequence coverage in %",
			HorizontalAlignmentSharedConstant.ALIGN_CENTER),
	//
	PROTEIN_SEQUENCE_COVERAGE_IMG("Coverage graph", "Protein coverage graph",
			"Graphical representation of sequence coverage", HorizontalAlignmentSharedConstant.ALIGN_LEFT),
	//
	PROTEIN_LENGTH("Length", "Length", "Length of the protein sequence",
			HorizontalAlignmentSharedConstant.ALIGN_CENTER),
	//
	MOL_W("Molecular weight", "MolWt", "Protein molecular weight", HorizontalAlignmentSharedConstant.ALIGN_CENTER),
	//
	PROTEIN_PI("Isoelectric Point", "PI", "Isoelectric point of the protein",
			HorizontalAlignmentSharedConstant.ALIGN_CENTER),
	//
	GENE("Gene name", "Gene", "Associated gene name", HorizontalAlignmentSharedConstant.ALIGN_CENTER),
	//
	PROTEIN_AMOUNT("Protein amounts", "Prot. Am.", "Protein amounts", false),
	//
	PROTEIN_RATIO("Protein ratios", "Prot. Ratios.", "Protein amount ratios", false,
			HorizontalAlignmentSharedConstant.ALIGN_CENTER),
	//
	PROTEIN_RATIO_SCORE("Ratio score", "Ratio sc.", "Score or confident value associated to the protein ratio", false,
			HorizontalAlignmentSharedConstant.ALIGN_CENTER),
	//
	PSM_AMOUNT("Psm amounts", "Psm. Am.", "Psm amounts", false, HorizontalAlignmentSharedConstant.ALIGN_CENTER),
	//
	PSM_RATIO("Psm ratios", "Psm. Ratios.", "Psm amount ratios", false, HorizontalAlignmentSharedConstant.ALIGN_CENTER), // /
	//
	PSM_RATIO_SCORE("Ratio score", "Ratio sc.", "Score or confident value associated to the psm ratio", false,
			HorizontalAlignmentSharedConstant.ALIGN_CENTER),
	//
	PSM_SCORE("PSM scores", "PSM sc.", "PSM scores", false, HorizontalAlignmentSharedConstant.ALIGN_CENTER),
	//
	PEPTIDE_AMOUNT("Peptide amounts", "Pepm. Am.", "Peptide amounts", false,
			HorizontalAlignmentSharedConstant.ALIGN_CENTER),
	//
	PEPTIDE_RATIO("Peptide ratios", "Pep. Ratios.", "Peptide amount ratios", false,
			HorizontalAlignmentSharedConstant.ALIGN_CENTER), //
	//
	PEPTIDE_RATIO_SCORE("Ratio score", "Ratio sc.", "Score or confident value associated to the peptide ratio", false,
			HorizontalAlignmentSharedConstant.ALIGN_CENTER),
	//
	PEPTIDE_SCORE("Peptide scores", "Pep sc.", "Peptide scores", false, HorizontalAlignmentSharedConstant.ALIGN_CENTER),
	//
	PEPTIDE_SEQUENCE("Peptide sequence", "Seq.", "Peptide sequence"),
	//
	PEPTIDE_LENGTH("Peptide length", "Len.", "Length of the peptide sequence",
			HorizontalAlignmentSharedConstant.ALIGN_CENTER),
	//
	NUM_PTMS("# Different modifications", "# Modif.", "Number of different modifications",
			HorizontalAlignmentSharedConstant.ALIGN_CENTER),
	//
	NUM_PTM_SITES("# Different modification sites", "# Modif. sites", "Number of modification sites",
			HorizontalAlignmentSharedConstant.ALIGN_CENTER),
	//
	PTMS("PTMs", "PTMs", "Peptide Modifications"),
	//
	PTM_SCORE("PTM score", "PTM Sc.", "Modification confidence score", false,
			HorizontalAlignmentSharedConstant.ALIGN_CENTER),
	//
	PEPTIDE_PI("Isoelectric Point", "PI", "Isoelectric point of the peptide sequence",
			HorizontalAlignmentSharedConstant.ALIGN_CENTER),

	//
	PSM_ID("PSM Identifier", "PSM ID", "Identifier of the Peptide Spectrum Match"),

	//
	PSM_RUN_ID("Run Identifier", "Run ID", "Identifier of the MS Run in which the PSM was identified"),
	//
	PROTEIN_GROUP_TYPE("Protein evidence", "Evidence",
			"Evidence of the protein: conclusive, non conclusive, ambiguous or indistinghisable",
			HorizontalAlignmentSharedConstant.ALIGN_CENTER),
	//
	NUM_PROTEIN_GROUP_MEMBERS("# Group members", "# Prot./group", "Number of proteins in the group",
			HorizontalAlignmentSharedConstant.ALIGN_CENTER),
	//
	TAXONOMY("Taxonomy", "Tax", "Taxonomy of the protein", HorizontalAlignmentSharedConstant.ALIGN_CENTER),
	//
	PROJECT("Project(s)", "Prj.", "Project or projects in which has been detected"),
	//
	CHARGE("Charge", "Z", "Charge state", HorizontalAlignmentSharedConstant.ALIGN_CENTER),
	//
	PROTEIN_FUNCTION("Function", "Function", "Annotated protein function"),
	//
	SECONDARY_ACCS("Secondary Accessions", "Sec.ACCs.", "Secondary protein accessions"),
	//
	POSITION_IN_PROTEIN("Start position(s)", "Start", "Position or positions of the peptide in the protein sequence",
			HorizontalAlignmentSharedConstant.ALIGN_CENTER),
	//
	CONDITION("Experimental condition(s)", "Condition", "Experimental condition(s) in which has been detected"),
	//
	OMIM("OMIM reference", "OMIM", "Reference to Online Mendelian Inheritance in Man"),
	//
	PEPTIDES_TABLE_BUTTON("Show peptide sharing table", "-", "Button to show the peptide sharing table",
			HorizontalAlignmentSharedConstant.ALIGN_CENTER),
	//
	PEPTIDE_EVIDENCE("Peptide evidence", "evidence", "Peptide evidence according to its uniqueness",
			HorizontalAlignmentSharedConstant.ALIGN_CENTER),
	//
	UNIPROT_PROTEIN_EXISTENCE("Protein Existence", "PE", "UniProtKB protein existence",
			HorizontalAlignmentSharedConstant.ALIGN_CENTER), //
	PSM_RATIO_GRAPH("Ratio Graph", "R", "Graphical representation of the ratio", false,
			HorizontalAlignmentSharedConstant.ALIGN_CENTER), //
	PEPTIDE_RATIO_GRAPH("Ratio Graph", "R", "Graphical representation of the ratio", false,
			HorizontalAlignmentSharedConstant.ALIGN_CENTER), //
	PROTEIN_RATIO_GRAPH("Ratio Graph", "R", "Graphical representation of the ratio", false,
			HorizontalAlignmentSharedConstant.ALIGN_CENTER), //

	// REACTOME TABLE COLUMNS
	PATHWAY_NAME("PathWay", "Pathway", "PathWay name"), //
	PATHWAY_FDR("Pathway FDR", "FDR", "FDR of the enrichment of the pathway"), //
	PATHWAY_PVALUE("Pathway p-value", "p-value", "p-value of the enrichment of the pathway"), //
	PATHWAY_ENTITIES_RATIO("Ratio of found entities", "E.Ratio", "Ratio of found entities"), //
	PATHWAY_REACTIONS_RATIO("Ratio of found reactions", "R.Ratio", "Ratio of found reactions"), //
	PATHWAY_RESOURCE("Pathway resource", "Resource", "Pathway resource"), //
	PATHWAY_ENTITIES_FOUND("Entities found", "E.Found", "Number of entities found"), //
	PATHWAY_REACTIONS_FOUND("Reactions found", "R.Found", "Number of reactions found"), //
	PATHWAY_ENTITIES_TOTAL("Total entities", "E.Total", "Number of total entities"), //
	PATHWAY_REACTIONS_TOTAL("Total reactions", "R.Total", "Number of total reactions"), //
	PATHWAY_ID("Pathway ID", "ID", "Pathway ID"),
	// , SPC_BY_RUN("SPC by run",
	// "SPC by run", "Spectral counts per run");
	// UNIPROT FEATURES FOR PROTEINS
	PROTEIN_DOMAIN_FAMILIES("Family & Domains", "Family & Domains",
			"UniProt annnotation for providing information on sequence similarities with other proteins and the domain(s) present in a protein."), //
	PROTEIN_ACTIVE_SITE("Functional sites", "Funct. sites", "UniProt annnotation for protein functional sites"), //
	PROTEIN_MOLECULAR_PROCESSING("Molecular Processing", "Processing",
			"UniProt annnotation for describing processing events."), //
	PROTEIN_PTM("Protein PTMs", "PTMs", "UniProt annnotation for described aminoacid modifications in the protein"), //
	PROTEIN_NATURAL_VARIATIONS("Natural Variations", "Nat. variations",
			"UniProt annnotation for amino acid change(s) producing alternate protein isoforms / Description of a natural variant of the protein"), //
	PROTEIN_SECONDARY_STRUCTURE("Secondary structure", "Sec. Struct.",
			"UniProt annnotation for protein secondary structure (helix, beta strand or turn)"), //
	PROTEIN_EXPERIMENTAL_INFO("Experimental info", "Exp. Info", "UniProt experimental information"), //
	// UNIPROT FEATURES FOR PEPTIDES
	PEPTIDE_DOMAIN_FAMILIES("Family & Domains", "Family & Domains",
			"UniProt annnotation for providing information on sequence similarities with other proteins and the domain(s) present in a PEPTIDE."), //
	PEPTIDE_ACTIVE_SITE("Functional sites", "Funct. sites", "UniProt annnotation for protein functional sites"), //
	PEPTIDE_MOLECULAR_PROCESSING("Molecular Processing", "Processing",
			"UniProt annnotation for describing processing events."), //
	PEPTIDE_PTM("Protein PTMs", "PTMs", "UniProt annnotation for described aminoacid modifications in the protein"), //
	PEPTIDE_NATURAL_VARIATIONS("Natural Variations", "Nat. variations",
			"UniProt annnotation for amino acid change(s) producing alternate protein isoforms / Description of a natural variant of the protein"), //
	PEPTIDE_SECONDARY_STRUCTURE("Secondary structure", "Sec. Struct.",
			"UniProt annnotation for protein secondary structure (helix, beta strand or turn)"), //
	PEPTIDE_EXPERIMENTAL_INFO("Experimental info", "Exp. Info", "UniProt experimental information"),

	// link to pride cluster
	LINK_TO_PRIDE_CLUSTER("Link to PRIDE cluster (EBI)", "PRIDE", "Link to information in PRIDE cluster (EBI)",
			HorizontalAlignmentSharedConstant.ALIGN_CENTER),
	// link to intAct
	LINK_TO_INTACT("Link to IntAct (EBI)", "IntAct",
			"Link to information in IntAct Molecular Interaction Database (EBI)",
			HorizontalAlignmentSharedConstant.ALIGN_CENTER),
	// link to Complex portal
	LINK_TO_COMPLEX_PORTAL("Link to Complex Portal (EBI)", "Complex",
			"Link to information in Complex Portal Database (EBI)", HorizontalAlignmentSharedConstant.ALIGN_CENTER),
	// link to Reactome
	REACTOME_ID_LINK("Link to Reactome Pathways", "Pathways", "Link to Reactome Pathways",
			HorizontalAlignmentSharedConstant.ALIGN_LEFT);
	private final String name;
	private final String abr;
	private final String description;
	private HorizontalAlignmentSharedConstant horizontalAlignment;
	private boolean addColumnByDefault;

	private ColumnName(String name, String abr, String description) {
		this(name, abr, description, true);
	}

	private ColumnName(String name, String abr, String description, boolean addColumnByDefault) {
		this(name, abr, description, addColumnByDefault, HorizontalAlignmentSharedConstant.ALIGN_LEFT);
	}

	private ColumnName(String name, String abr, String description,
			HorizontalAlignmentSharedConstant horizontalAlignment) {
		this(name, abr, description, true, horizontalAlignment);
	}

	private ColumnName(String name, String abr, String description, boolean addColumnByDefault,
			HorizontalAlignmentSharedConstant horizontalAlignment) {
		this.abr = abr;
		this.description = description;
		this.name = name;
		this.horizontalAlignment = horizontalAlignment;
		setAddColumnByDefault(addColumnByDefault);
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @return the abr
	 */
	public String getAbr() {
		return abr;
	}

	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}

	public static ColumnName getByPropertyName(String name) {
		final ColumnName[] values = values();
		for (ColumnName columnName : values) {
			if (columnName.name().equals(name))
				return columnName;
		}
		return null;
	}

	public static ColumnName getByName(String name) {
		final ColumnName[] values = values();
		for (ColumnName columnName : values) {
			if (columnName.getName().equals(name))
				return columnName;
		}
		return null;
	}

	public static ColumnName getByAbr(String abr) {
		final ColumnName[] values = values();
		for (ColumnName columnName : values) {
			if (columnName.getAbr().equals(abr))
				return columnName;
		}
		return null;
	}

	public static ColumnName getByDescription(String description) {
		final ColumnName[] values = values();
		for (ColumnName columnName : values) {
			if (columnName.getDescription().equals(description))
				return columnName;
		}
		return null;
	}

	public HorizontalAlignmentSharedConstant getHorizontalAlignment() {
		return horizontalAlignment;
	}

	/**
	 * Based on the sortBy parameter needed in restfull webservice at
	 * http://www.reactome.org/pages/documentation/developer-guide/analysis-
	 * service/api/
	 *
	 * @param column
	 * @return
	 */
	public static String getReactomeAnalysisPathwayColumnName(ColumnName column) {
		switch (column) {
		case PATHWAY_ENTITIES_FOUND:
			return "FOUND_ENTITIES";
		case PATHWAY_ENTITIES_RATIO:
			return "ENTITIES_RATIO";
		case PATHWAY_ENTITIES_TOTAL:
			return "TOTAL_ENTITIES";
		case PATHWAY_FDR:
			return "ENTITIES_FDR";
		case PATHWAY_NAME:
			return "NAME";
		case PATHWAY_PVALUE:
			return "ENTITIES_PVALUE";
		case PATHWAY_REACTIONS_FOUND:
			return "FOUND_REACTIONS";
		case PATHWAY_REACTIONS_RATIO:
			return "REACTIONS_RATIO";
		case PATHWAY_REACTIONS_TOTAL:
			return "TOTAL_REACTIONS";

		default:
			return null;
		}

	}

	/**
	 * @return the addColumnByDefault
	 */
	public boolean isAddColumnByDefault() {
		return addColumnByDefault;
	}

	/**
	 * @param addColumnByDefault
	 *            the addColumnByDefault to set
	 */
	public void setAddColumnByDefault(boolean addColumnByDefault) {
		this.addColumnByDefault = addColumnByDefault;
	}
}
