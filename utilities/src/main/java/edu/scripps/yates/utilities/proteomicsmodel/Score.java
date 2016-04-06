package edu.scripps.yates.utilities.proteomicsmodel;

public interface Score {
	public String getValue();

	/**
	 * Custom name of the score
	 * 
	 * @return
	 */
	public String getScoreName();

	/**
	 * CV of a score
	 * 
	 * @return
	 */
	public String getScoreType();

	public String getScoreDescription();

}
