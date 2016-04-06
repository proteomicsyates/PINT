package edu.scripps.yates.proteindb.queries.dataproviders;

import java.util.Map;
import java.util.Set;

import edu.scripps.yates.proteindb.persistence.mysql.Protein;
import edu.scripps.yates.proteindb.persistence.mysql.Psm;

/**
 * This interface provides the method for a system that retrieves the proteins
 * from a DATABASE system
 *
 * @author Salva
 *
 */
public interface ProteinProviderFromDB {

	/**
	 * Gets a protein Map using the primary accession as key
	 *
	 * @return
	 */

	public Map<String, Set<Protein>> getProteinMap();

	/**
	 * Gets a psm Map using the peptide sequence as key
	 *
	 * @return
	 */

	public Map<String, Set<Psm>> getPsmMap();

	/**
	 * Set a constriction in the protein provider to only take proteins from the
	 * projects in the parameter
	 *
	 * @param projectTags
	 */
	public void setProjectTags(Set<String> projectTags);

}
