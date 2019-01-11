package edu.scripps.yates.shared.model;

public enum ProteinEvidence {
	/**
	 * At least one unique peptides
	 */
	CONCLUSIVE("Protein that contains at least one unique peptides"),
	/**
	 * Only NonDiscrimitating peptides
	 */
	NONCONCLUSIVE("Protein group sharing not discriminating peptides"),
	/**
	 * Shared Discriminating peptides
	 */
	AMBIGUOUSGROUP("Protein group sharing at least one discriminant peptide"),
	/**
	 * Same peptides and at least one Discriminating
	 */
	INDISTINGUISHABLE("Protein group sharing the whole set of peptides"),
	/**
	 * No peptides
	 */
	FILTERED("Filtered out group");

	private final String definition;

	ProteinEvidence(String definition) {
		this.definition = definition;
	}

	/**
	 * @return the definition
	 */
	public String getDefinition() {
		return definition;
	}
}
