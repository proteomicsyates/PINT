package edu.scripps.yates.proteindb.queries.dataproviders;

import java.util.Map;
import java.util.Set;

import edu.scripps.yates.proteindb.persistence.mysql.Peptide;
import edu.scripps.yates.proteindb.persistence.mysql.Protein;
import edu.scripps.yates.proteindb.persistence.mysql.Psm;

/**
 * This interface provides the method for a system that retrieves the proteins,
 * peptides and Psms from a DATABASE system
 *
 * @author Salva
 *
 */
public interface DataProviderFromDB {

	/**
	 * Gets a protein Map using the primary accession as key
	 *
	 * @param testMode
	 *
	 * @return
	 */

	public Map<String, Set<Protein>> getProteinMap(boolean testMode);

	/**
	 * Gets a psm Map using the peptide sequence as key
	 *
	 * @return
	 */

	public Map<String, Set<Psm>> getPsmMap(boolean testMode);

	/**
	 * Gets a peptide Set
	 *
	 * @return
	 */

	public Set<Peptide> getPeptideSet(boolean testMode);

	/**
	 * Set a constriction in the protein provider to only take proteins from the
	 * projects in the parameter
	 *
	 * @param projectTags
	 */
	public void setProjectTags(Set<String> projectTags);

}
