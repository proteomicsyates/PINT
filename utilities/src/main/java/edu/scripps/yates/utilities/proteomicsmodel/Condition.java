package edu.scripps.yates.utilities.proteomicsmodel;

import java.util.Set;

public interface Condition {

	public Sample getSample();

	public String getUnit();

	public Double getValue();

	public String getDescription();

	public String getName();

	public Project getProject();

	/**
	 * Gets the proteins in one experimental condition. Note that the list may
	 * contain repeated proteins. Call to {@link ProteomicsModelUtils} mergeProteins for
	 * grouping the proteins by its primery accession.
	 *
	 * @return
	 */
	public Set<Protein> getProteins();

	public Set<PSM> getPSMs();

	public Set<Peptide> getPeptides();

}
