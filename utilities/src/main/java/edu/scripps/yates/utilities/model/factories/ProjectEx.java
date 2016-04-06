package edu.scripps.yates.utilities.model.factories;

import java.io.Serializable;
import java.net.URL;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import edu.scripps.yates.utilities.proteomicsmodel.Condition;
import edu.scripps.yates.utilities.proteomicsmodel.MSRun;
import edu.scripps.yates.utilities.proteomicsmodel.Project;

public class ProjectEx implements Project, Serializable {

	/**
	 *
	 */
	private static final long serialVersionUID = 6231680897122500198L;
	private final String description;
	private final String name;
	private Date releaseDate;

	private URL pubmedLink;
	private final Set<Condition> conditions = new HashSet<Condition>();
	private final Set<MSRun> msRuns = new HashSet<MSRun>();

	private boolean isPrivate = true;
	private String tag;
	private Date uploadedDate;
	private boolean big;

	public ProjectEx(String name, String description) {
		super();
		this.description = description;
		this.name = name;
	}

	/**
	 * @param releaseDate
	 *            the releaseDate to set
	 */
	public void setReleaseDate(Date releaseDate) {
		this.releaseDate = releaseDate;
	}

	/**
	 * @param pubmedLink
	 *            the pubmedLink to set
	 */
	public void setPubmedLink(URL pubmedLink) {
		this.pubmedLink = pubmedLink;
	}

	@Override
	public String getDescription() {
		return description;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public Date getReleaseDate() {
		return releaseDate;
	}

	@Override
	public URL getPubmedLink() {
		return pubmedLink;
	}

	@Override
	public Set<Condition> getConditions() {
		return conditions;
	}

	public void addExperimentalCondition(Condition condition) {
		conditions.add(condition);
	}

	@Override
	public boolean isPrivate() {
		return isPrivate;
	}

	/**
	 * @param isPrivate
	 *            the isPrivate to set
	 */
	public void setPrivate(boolean isPrivate) {
		this.isPrivate = isPrivate;
	}

	@Override
	public String getTag() {
		return tag;
	}

	@Override
	public Date getUploadedDate() {
		return uploadedDate;
	}

	/**
	 * @param tag
	 *            the tag to set
	 */
	public void setTag(String tag) {
		this.tag = tag;
	}

	/**
	 * @param uploadedDate
	 *            the uploadedDate to set
	 */
	public void setUploadedDate(Date uploadedDate) {
		this.uploadedDate = uploadedDate;
	}

	/**
	 * @return the msRuns
	 */
	public Set<MSRun> getMsRuns() {
		return msRuns;
	}

	@Override
	public Set<MSRun> getMSRuns() {
		return msRuns;
	}

	/**
	 * @return the big
	 */
	@Override
	public boolean isBig() {
		return big;
	}

	/**
	 * @param big
	 *            the big to set
	 */
	public void setBig(boolean big) {
		this.big = big;
	}

}
