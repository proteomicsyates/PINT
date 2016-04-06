package edu.scripps.yates.shared.model;

import java.io.Serializable;

public enum PeptideRelation implements Serializable {

	UNIQUE("Peptides that can only be assigned to a single protein in the database"),
	//
	NONDISCRIMINATING("Shared peptides which presence is explained by proteins with unique or discriminating peptides"),

	//
	DISCRIMINATING("Shared peptides which presence is explained by a set of proteins without unique peptides");

	private final String definition;

	PeptideRelation(String definition) {
		this.definition = definition;
	}

	/**
	 * @return the definition
	 */
	public String getDefinition() {
		return definition;
	}

	public static PeptideRelation getPeptideRelationByName(String name) {
		for (PeptideRelation relation : PeptideRelation.values()) {
			if (relation.name().equalsIgnoreCase(name)) {
				return relation;
			}
		}
		return null;
	}
}
