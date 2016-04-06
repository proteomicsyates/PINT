package edu.scripps.yates.annotations.uniprot;

public enum UniprotProteinExistence {
	PROTEIN_LEVEL(1, "Experimental evidence at protein level", "Protein"), TRANSCRIPT_LEVEL(2,
			"Experimental evidence at transcript level",
			"Transcript"), HOMOLOGY_INFERRED(3, "Protein inferred from homology", "Homology"), PREDICTED(4,
					"Protein predicted", "Predicted"), UNCERTAIN(5, "Protein uncertain", "Uncertain");

	private final int level;
	private final String description;
	private final String name;

	private UniprotProteinExistence(int level, String description, String name) {
		this.level = level;
		this.description = description;
		this.name = name;
	}

	/**
	 * @return the level
	 */
	public int getLevel() {
		return level;
	}

	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

}
