package edu.scripps.yates.agent;

import java.util.Map;
import java.util.Set;

/**
 * This interface provides the method for a system that retrieves the proteins
 * even from an Excel table and from a DATABASE system
 * 
 * @author Salva
 * 
 */
public interface DataProvider<T> {

	/**
	 * Gets a protein Map using the primary accession as key
	 * 
	 * @return
	 */
	public Map<String, Set<T>> getProteinMap();

}
