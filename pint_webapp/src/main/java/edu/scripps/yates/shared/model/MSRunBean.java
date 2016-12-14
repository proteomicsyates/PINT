package edu.scripps.yates.shared.model;

import java.io.Serializable;
import java.util.Date;

import edu.scripps.yates.shared.model.interfaces.HasId;

public class MSRunBean extends HasId implements Serializable {
	/**
	 *
	 */
	private static final long serialVersionUID = -247769229381526464L;
	private String runID;
	private String path;
	private Date date;
	private Integer dbID;
	private String fastaFileRef;
	private String enzymeResidues;
	private String enzymeNocutResidues;
	private ProjectBean project;

	public MSRunBean() {

	}

	/**
	 * @return the serialversionuid
	 */
	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	/**
	 * @param path
	 *            the path to set
	 */
	public void setPath(String path) {
		this.path = path;
	}

	public MSRunBean(String runID, String path) {
		this.path = path;
		this.runID = runID;
	}

	/**
	 * @return the runID
	 */
	public String getRunID() {
		return runID;
	}

	/**
	 * @param runID
	 *            the runID to set
	 */
	public void setRunID(String runID) {
		this.runID = runID;
	}

	/**
	 * @return the name
	 */

	public String getPath() {
		return path;
	}

	/**
	 * @return the date
	 */

	public Date getDate() {
		return date;
	}

	/**
	 * @param date
	 *            the date to set
	 */
	public void setDate(Date date) {
		this.date = date;
	}

	/*
	 * Are equals if the id is the same
	 */

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof MSRunBean)
			return getRunID().equals(((MSRunBean) obj).getRunID());
		return super.equals(obj);
	}

	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		return -1;
	}

	/**
	 * @return the dbID
	 */
	public Integer getDbID() {
		return dbID;
	}

	/**
	 * @param dbID
	 *            the dbID to set
	 */
	public void setDbID(Integer dbID) {
		this.dbID = dbID;
	}

	@Override
	public String getId() {
		return getRunID();
	}

	public void setFastaFileRef(String fastaFileRef) {
		this.fastaFileRef = fastaFileRef;

	}

	public void setEnzymeResidues(String enzymeResidues) {
		this.enzymeResidues = enzymeResidues;

	}

	public void setEnzymeNoCutResidues(String enzymeNocutResidues) {
		this.enzymeNocutResidues = enzymeNocutResidues;
	}

	/**
	 * @return the enzymeNocutResidues
	 */
	public String getEnzymeNocutResidues() {
		return enzymeNocutResidues;
	}

	/**
	 * @param enzymeNocutResidues
	 *            the enzymeNocutResidues to set
	 */
	public void setEnzymeNocutResidues(String enzymeNocutResidues) {
		this.enzymeNocutResidues = enzymeNocutResidues;
	}

	/**
	 * @return the fastaFileRef
	 */
	public String getFastaFileRef() {
		return fastaFileRef;
	}

	/**
	 * @return the enzymeResidues
	 */
	public String getEnzymeResidues() {
		return enzymeResidues;
	}

	/**
	 * @return the project
	 */
	public ProjectBean getProject() {
		return project;
	}

	/**
	 * @param project
	 *            the project to set
	 */
	public void setProject(ProjectBean project) {
		this.project = project;
	}

}
