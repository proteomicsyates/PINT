package edu.scripps.yates.proteindb.queries.semantic;

public enum Command {
	// EXPERIMENT CONDITION
	CONDITION_PROJECT("COND", "COND[Condition_name, Project_tag]"),
	// ANNOTATION
	COMPLEX_ANNOTATION("CAN",
			"CAN[Uniprot_version, Uniprot_header_line, Annotation_type, Annotation_name, Annotation_value, Numerical_condition]"),
			// SIMPLE_ANNOTATION
			SIMPLE_ANNOTATION("AN", "AN[uniprot_version, Annotation_String, Numerical_condition]"),

	// SCORE
			SCORE("SC", "SC[Aggregation_level, Score_type, Score_name, Numerical_condition]"),
			// RATIO
			RATIO("RA",
					"RA[Aggregation_level, " + Command.CONDITION_PROJECT.getFormat() + ","
							+ Command.CONDITION_PROJECT.getFormat() + ", Ratio_name, Numerical_condition, "
							+ Command.SCORE.getFormat() + "]"),
							// AMOUNT
							AMOUNT("AM", "AM[Aggregation_level, Amount_type, " + Command.CONDITION_PROJECT.getFormat()
									+ ", Numerical_condition]"),
									// THRESHOLD
									THRESHOLD("THR", "THR[threshold_name, Boolean_value]"),
									// GENE_NAME
									GENE_NAME("GN", "GN[gene_name]"),
									// LABEL
									LABEL("LB", "LB[Aggregation_level, Label_name, ONLY]"),
									// TAXONOMY
									TAXONOMY("TX", "TX[Aggregation_level, Organism_name, Ncbi_tax_id, ONLY]"),
									// PROTEINACCESSION
									PROTEIN_ACC("ACC", "ACC[CSV_accessions]"),
									// MSRUN
									MSRUN("MSRUN", "MSRUN[CSV_MS_run_Ids]"),
									// PTM
									PTM("PTM",
											"PTM[PTM_name, PTM_mass_diff_Dalton, Dalton_tolerance, Numerical_condition]");

	private final String abbreviation;
	private final String format;

	Command(String abbreviation, String format) {
		this.abbreviation = abbreviation;
		this.format = format;
	}

	public static Command getCommand(String commandWord) {
		final Command[] values = Command.values();
		for (Command command : values) {

			if (command.getAbbreviation().equalsIgnoreCase(commandWord))
				return command;
		}
		return null;
	}

	/**
	 * @return the abbreviation
	 */
	public String getAbbreviation() {
		return abbreviation;
	}

	/**
	 * @return the format
	 */
	public String getFormat() {
		return format;
	}

	public static String getCommands() {
		StringBuilder sb = new StringBuilder();

		final Command[] values = Command.values();
		for (Command command : values) {
			if (!"".equals(sb.toString()))
				sb.append(",");
			sb.append(command.getAbbreviation());
		}
		return sb.toString();
	}

}
