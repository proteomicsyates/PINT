package edu.scripps.yates.shared.model;

import edu.scripps.yates.shared.exceptions.PintException.PINT_ERROR_TYPE;
import edu.scripps.yates.shared.exceptions.PintRuntimeException;

public enum UniprotProteinExistence {
	PROTEIN_LEVEL(1, "Experimental evidence at protein level", "Evidence at protein level",
			"Protein"), TRANSCRIPT_LEVEL(2, "Experimental evidence at transcript level", "Evidence at transcript level",
					"Transcript"), HOMOLOGY_INFERRED(3, "Protein inferred from homology", "Inferred from homology",
							"Homology"), PREDICTED(4, "Protein predicted", "Predicted",
									"Predicted"), UNCERTAIN(5, "Protein uncertain", "Uncertain", "Uncertain");

	private final int level;
	private final String description;
	private final String key;
	private final String name;

	UniprotProteinExistence(int level, String description, String key, String name) {
		this.level = level;
		this.description = description;
		this.key = key;
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

	public static UniprotProteinExistence getFromValue(String value) {
		for (UniprotProteinExistence upe : values()) {
			if (upe.getKey().equalsIgnoreCase(value)) {
				return upe;
			}
		}
		throw new PintRuntimeException("The value '" + value + "' is not supported as a valid "
				+ UniprotProteinExistence.class.getCanonicalName(), PINT_ERROR_TYPE.VALUE_NOT_SUPPORTED);
	}

	public String getKey() {
		return key;
	}
}
