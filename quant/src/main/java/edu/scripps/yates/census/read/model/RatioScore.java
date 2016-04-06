package edu.scripps.yates.census.read.model;

import edu.scripps.yates.utilities.proteomicsmodel.Score;

public class RatioScore implements Score {
	private String value;
	private String scoreName;
	private String scoreType;
	private String scoreDescription;

	public RatioScore(String value, String scoreName, String scoreType, String scoreDescription) {
		super();
		this.value = value;
		this.scoreName = scoreName;
		this.scoreType = scoreType;
		this.scoreDescription = scoreDescription;
	}

	/**
	 * @return the value
	 */
	@Override
	public String getValue() {
		return value;
	}

	/**
	 * @param value
	 *            the value to set
	 */
	public void setValue(String value) {
		this.value = value;
	}

	/**
	 * @return the scoreName
	 */
	@Override
	public String getScoreName() {
		return scoreName;
	}

	/**
	 * @param scoreName
	 *            the scoreName to set
	 */
	public void setScoreName(String scoreName) {
		this.scoreName = scoreName;
	}

	/**
	 * @return the scoreType
	 */
	@Override
	public String getScoreType() {
		return scoreType;
	}

	/**
	 * @param scoreType
	 *            the scoreType to set
	 */
	public void setScoreType(String scoreType) {
		this.scoreType = scoreType;
	}

	/**
	 * @return the scoreDescription
	 */
	@Override
	public String getScoreDescription() {
		return scoreDescription;
	}

	/**
	 * @param scoreDescription
	 *            the scoreDescription to set
	 */
	public void setScoreDescription(String scoreDescription) {
		this.scoreDescription = scoreDescription;
	}

}
