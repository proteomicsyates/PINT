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
	PROTEIN_AMOUNT("Protein amounts", "Prot. Am.", "Protein amounts"),
	//
	PROTEIN_RATIO("Protein ratios", "Prot. Ratios.", "Protein amount ratios",
			HorizontalAlignmentSharedConstant.ALIGN_CENTER),
	//
	PROTEIN_RATIO_SCORE("Ratio score", "Ratio sc.", "Score or confident value associated to the protein ratio",
			HorizontalAlignmentSharedConstant.ALIGN_CENTER),
	//
	PSM_AMOUNT("Psm amounts", "Psm. Am.", "Psm amounts", HorizontalAlignmentSharedConstant.ALIGN_CENTER),
	//
	PSM_RATIO("Psm ratios", "Psm. Ratios.", "Psm amount ratios", HorizontalAlignmentSharedConstant.ALIGN_CENTER), // /
	//
	PSM_RATIO_SCORE("Ratio score", "Ratio sc.", "Score or confident value associated to the psm ratio",
			HorizontalAlignmentSharedConstant.ALIGN_CENTER),
	//
	PSM_SCORE("PSM scores", "PSM sc.", "PSM scores", HorizontalAlignmentSharedConstant.ALIGN_CENTER),
	//
	PEPTIDE_AMOUNT("Peptide amounts", "Pepm. Am.", "Peptide amounts", HorizontalAlignmentSharedConstant.ALIGN_CENTER),
	//
	PEPTIDE_RATIO("Peptide ratios", "Pep. Ratios.", "Peptide amount ratios",
			HorizontalAlignmentSharedConstant.ALIGN_CENTER), //
	//
	PEPTIDE_RATIO_SCORE("Ratio score", "Ratio sc.", "Score or confident value associated to the peptide ratio",
			HorizontalAlignmentSharedConstant.ALIGN_CENTER),
	//
	PEPTIDE_SCORE("Peptide scores", "Pep sc.", "Peptide scores", HorizontalAlignmentSharedConstant.ALIGN_CENTER),
	//
	PEPTIDE_SEQUENCE("Peptide sequence", "Seq.", "Peptide sequence"),
	//
	PEPTIDE_LENGTH("Peptide length", "Len.", "Length of the peptide sequence"),
	//
	NUM_PTMS("# Different modifications", "# Modif.", "Number of different modifications",
			HorizontalAlignmentSharedConstant.ALIGN_CENTER),
	//
	NUM_PTM_SITES("# Different modification sites", "# Modif. sites", "Number of modification sites",
			HorizontalAlignmentSharedConstant.ALIGN_CENTER),
	//
	PTMS("PTMs", "PTMs", "Peptide Modifications"),
	//
	PTM_SCORE("PTM score", "PTM Sc.", "Modification confidence score", HorizontalAlignmentSharedConstant.ALIGN_CENTER),
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
	PSM_RATIO_GRAPH("Ratio Graph", "R", "Graphical representation of the ratio",
			HorizontalAlignmentSharedConstant.ALIGN_CENTER), //
	PEPTIDE_RATIO_GRAPH("Ratio Graph", "R", "Graphical representation of the ratio",
			HorizontalAlignmentSharedConstant.ALIGN_CENTER), //
	PROTEIN_RATIO_GRAPH("Ratio Graph", "R", "Graphical representation of the ratio",
			HorizontalAlignmentSharedConstant.ALIGN_CENTER);
	// , SPC_BY_RUN("SPC by run",
	// "SPC by run", "Spectral counts per run");

	private final String name;
	private final String abr;
	private final String description;
	private HorizontalAlignmentSharedConstant horizontalAlignment;

	private ColumnName(String name, String abr, String description) {
		this.abr = abr;
		this.description = description;
		this.name = name;
		horizontalAlignment = HorizontalAlignmentSharedConstant.ALIGN_LEFT;
	}

	private ColumnName(String name, String abr, String description,
			HorizontalAlignmentSharedConstant horizontalAlignment) {
		this.abr = abr;
		this.description = description;
		this.name = name;
		this.horizontalAlignment = horizontalAlignment;
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
}
