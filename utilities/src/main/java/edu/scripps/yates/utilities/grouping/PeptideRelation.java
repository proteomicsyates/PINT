package edu.scripps.yates.utilities.grouping;

/**
 *
 * @author gorka
 */
public enum PeptideRelation {
    /**
     * Peptides that can only be assigned to a single protein in the database
     */
    UNIQUE,
    /**
     * Shared peptides which presence is explained by a set of proteins without unique peptides
     */
    DISCRIMINATING,
    /**
     *  Shared peptides which presence is explained by proteins with unique or discriminating peptides
     */
    NONDISCRIMINATING
}
