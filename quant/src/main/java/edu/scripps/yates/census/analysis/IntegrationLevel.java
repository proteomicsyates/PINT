package edu.scripps.yates.census.analysis;

public enum IntegrationLevel {
	//
	ION("ion"), //
	//
	SPECTRUM("spectrum"), //
	//
	PEPTIDE("peptide"), //
	// average quant for a peptide in a replicate of an experiment
	PEPTIDE_REPLICATE_EXPERIMENT("peptide_replicate_experiment"), //
	// average quant for a peptide in an experiment (over replicates, if
	// available)
	PEPTIDE_REPLICATE("peptide_experiment"), //
	//
	PROTEIN("protein"), //
	// average quant for a protein in a replicate of an experiment
	PROTEIN_REPLICATE_EXPERIMENT("protein_replicate_experiment"), //
	// average quant for a protein in an experiment (over replicates, if
	// available)
	PROTEIN_EXPERIMENT("protein_experiment") //
	;
	private final String name;

	private IntegrationLevel(String name) {
		this.name = name;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

}
