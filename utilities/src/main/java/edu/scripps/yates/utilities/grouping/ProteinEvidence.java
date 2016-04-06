package edu.scripps.yates.utilities.grouping;

/**
 *
 * @author gorka
 */
public enum ProteinEvidence {
    /**
     * At least one unique peptides
     */
    CONCLUSIVE,
    /**
     * Only NonDiscrimitating peptides
     */
    NONCONCLUSIVE,
    /**
     * Shared Discriminating peptides
     */
    AMBIGUOUSGROUP,
    /**
     * Same peptides and at least one Discriminating
     */
    INDISTINGUISHABLE,
    /**
     * No peptides
     */
    FILTERED
}
