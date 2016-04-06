package edu.scripps.yates.utilities.proteomicsmodel;

import java.util.List;

public interface PTM {
	public Double getMassShift();

	public String getName();

	public String getCVId();

	/**
	 * In case of PTM ambiguity assignment, gets the complete list of PTM sites.
	 * If no ambiguity, the list will contain just one element
	 *
	 * @return
	 */
	public List<PTMSite> getPTMSites();
}
