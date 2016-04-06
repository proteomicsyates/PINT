package edu.scripps.yates.utilities.model.factories;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import edu.scripps.yates.utilities.proteomicsmodel.PTM;
import edu.scripps.yates.utilities.proteomicsmodel.PTMSite;

public class PTMEx implements PTM, Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4442800986087569468L;
	private final String name;
	private final double massShift;
	private String cvId;
	private List<PTMSite> ptmSites;

	public PTMEx(String name, double massShift) {
		this.name = name;
		this.massShift = massShift;
	}

	@Override
	public Double getMassShift() {
		return massShift;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public String getCVId() {
		return cvId;
	}

	@Override
	public List<PTMSite> getPTMSites() {
		return ptmSites;
	}

	/**
	 * @param cvId
	 *            the cvId to set
	 */
	public void setCvId(String cvId) {
		this.cvId = cvId;
	}

	/**
	 * @param ptmSites
	 *            the ptmSites to set
	 */
	public void setPtmSites(List<PTMSite> ptmSites) {
		this.ptmSites = ptmSites;
	}

	public void addPtmSite(PTMSite ptmSite) {
		if (ptmSites == null)
			ptmSites = new ArrayList<PTMSite>();
		ptmSites.add(ptmSite);
	}
}
