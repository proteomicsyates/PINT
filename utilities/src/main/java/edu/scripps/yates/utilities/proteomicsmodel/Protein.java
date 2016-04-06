package edu.scripps.yates.utilities.proteomicsmodel;

import java.util.List;
import java.util.Set;

import edu.scripps.yates.utilities.grouping.GroupableProtein;

/**
 * This class represents a Protein, that has been detected in one {@link MSRun}
 * and be measured ({@link Amount}) in that run.<br>
 * However, {@link Protein} may belongs to a different conditions.
 *
 * @author Salva
 *
 */
public interface Protein extends HasScores, HasRatios, HasAmounts, HasConditions, HasPsms, HasMSRun, GroupableProtein {

	public Accession getPrimaryAccession();

	public List<Accession> getSecondaryAccessions();

	/**
	 * @return the genes
	 */
	public Set<Gene> getGenes();

	/**
	 * @return the proteinAnnotations
	 */
	public Set<ProteinAnnotation> getAnnotations();

	/**
	 * Gets all the {@link Threshold}s that has been applied to the protein.
	 *
	 * @return
	 */
	public Set<Threshold> getThresholds();

	/**
	 * If a {@link Threshold} with the name indicated in the parameter has been
	 * applied to the protein it will return yes if it has been passed or false
	 * otherwise. <br>
	 * In case of the {@link Threshold} has not been applied to the
	 * {@link Protein}, it will return null value.
	 *
	 * @param thresholdName
	 * @return
	 */
	public Boolean passThreshold(String thresholdName);

	public Set<Peptide> getPeptides();

	public int getLength();

	public double getPi();

	public double getMW();

	public String getSequence();

	public Organism getOrganism();

	public void setOrganism(Organism organism);

	public void setMw(double mw);

	public void setPi(double pi);

	public void setLength(int length);

	public void addPeptide(Peptide peptide);

	public void addGene(Gene gene);
}
