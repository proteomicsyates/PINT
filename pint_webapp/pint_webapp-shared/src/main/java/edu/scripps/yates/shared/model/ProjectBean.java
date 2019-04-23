package edu.scripps.yates.shared.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import edu.scripps.yates.shared.model.interfaces.HasId;

public class ProjectBean extends HasId implements Serializable {

	/**
	 *
	 */
	private static final long serialVersionUID = 3810351430216977374L;
	private String name;
	private String description;
	private String pubmedLink;
	private Date releaseDate;
	private String tag;
	private Date uploadedDate;
	private Integer dbId;
	private boolean publicAvailable;
	private boolean big;
	private List<ExperimentalConditionBean> conditions = new ArrayList<ExperimentalConditionBean>();
	private List<MSRunBean> msRuns = new ArrayList<MSRunBean>();
	private boolean hidden;
	private PrincipalInvestigatorBean principalInvestigator;
	private List<String> instruments = new ArrayList<String>();

	public void setName(String name) {
		this.name = name;

	}

	public void setDescription(String description) {
		this.description = description;

	}

	public void setPubmedLink(String string) {
		pubmedLink = string;

	}

	public void setReleaseDate(Date releaseDate) {
		this.releaseDate = releaseDate;

	}

	/**
	 * @return the name
	 */
	@Override
	public String getId() {
		return name;
	}

	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @return the pubmedLink
	 */
	public String getPubmedLink() {
		return pubmedLink;
	}

	/**
	 * @return the releaseDate
	 */
	public Date getReleaseDate() {
		return releaseDate;
	}

	/**
	 * @return the tag
	 */
	public String getTag() {
		return tag;
	}

	/**
	 * @param tag the tag to set
	 */
	public void setTag(String tag) {
		this.tag = tag;
	}

	/**
	 * @return the uploadedDate
	 */
	public Date getUploadedDate() {
		return uploadedDate;
	}

	/**
	 * @param uploadedDate the uploadedDate to set
	 */
	public void setUploadedDate(Date uploadedDate) {
		this.uploadedDate = uploadedDate;
	}

	public Integer getDbId() {
		return dbId;
	}

	/**
	 * @param dbId the dbId to set
	 */
	public void setDbId(Integer dbId) {
		this.dbId = dbId;
	}

	/**
	 * @return the publicAvailable
	 */
	public boolean isPublicAvailable() {
		return publicAvailable;
	}

	/**
	 * @param publicAvailable the publicAvailable to set
	 */
	public void setPublicAvailable(boolean publicAvailable) {
		this.publicAvailable = publicAvailable;
	}

	/**
	 * @return the conditions
	 */
	public List<ExperimentalConditionBean> getConditions() {
		return conditions;
	}

	/**
	 * @param conditions the conditions to set
	 */
	public void setConditions(List<ExperimentalConditionBean> conditions) {
		this.conditions = conditions;
	}

	/**
	 * @return the big
	 */
	public boolean isBig() {
		return big;
	}

	/**
	 * @param big the big to set
	 */
	public void setBig(boolean big) {
		this.big = big;
	}

	public void setIsHidden(boolean hidden) {
		setHidden(hidden);

	}

	/**
	 * @return the hidden
	 */
	public boolean isHidden() {
		return hidden;
	}

	/**
	 * @param hidden the hidden to set
	 */
	public void setHidden(boolean hidden) {
		this.hidden = hidden;
	}

	/**
	 * @return the msRuns
	 */
	public List<MSRunBean> getMsRuns() {
		return msRuns;
	}

	/**
	 * @param msRuns the msRuns to set
	 */
	public void setMsRuns(List<MSRunBean> msRuns) {
		this.msRuns = msRuns;
	}

	public PrincipalInvestigatorBean getPrincipalInvestigator() {
		return principalInvestigator;
	}

	public void setPrincipalInvestigator(PrincipalInvestigatorBean principalInvestigator) {
		this.principalInvestigator = principalInvestigator;
	}

	public List<String> getInstruments() {
		return instruments;
	}

	public void setInstruments(List<String> instruments) {
		this.instruments = instruments;
	}

}
