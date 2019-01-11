package edu.scripps.yates.shared.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class PTMBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4442800986087569468L;
	private String name;
	private double massShift;

	/**
	 * @return the serialversionuid
	 */
	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	/**
	 * @return the cvId
	 */
	public String getCvId() {
		return cvId;
	}

	/**
	 * @return the ptmSites
	 */
	public List<PTMSiteBean> getPtmSites() {
		return ptmSites;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @param massShift the massShift to set
	 */
	public void setMassShift(double massShift) {
		this.massShift = massShift;
	}

	private String cvId;
	private List<PTMSiteBean> ptmSites;

	public PTMBean() {

	}

	public PTMBean(String name, double massShift) {
		this.name = name;
		this.massShift = massShift;
	}

	public Double getMassShift() {
		return massShift;
	}

	public String getName() {
		return name;
	}

	public String getCVId() {
		return cvId;
	}

	/**
	 * @param cvId the cvId to set
	 */
	public void setCvId(String cvId) {
		this.cvId = cvId;
	}

	/**
	 * @param ptmSites the ptmSites to set
	 */
	public void setPtmSites(List<PTMSiteBean> ptmSites) {
		this.ptmSites = ptmSites;
	}

	public void addPtmSite(PTMSiteBean ptmSite) {
		if (ptmSites == null)
			ptmSites = new ArrayList<PTMSiteBean>();
		ptmSites.add(ptmSite);
	}

	@Override
	public String toString() {
		// return this.name +
		// "("+NumberFormat.getFormat("#.###").format(this.massShift)+")"
		StringBuilder sb = new StringBuilder();
		for (PTMSiteBean ptmSite : ptmSites) {
			if (!"".equals(sb.toString()))
				sb.append(", ");
			sb.append(name + "(" + ptmSite.getPosition() + ")");
		}
		return sb.toString();
	}

}
