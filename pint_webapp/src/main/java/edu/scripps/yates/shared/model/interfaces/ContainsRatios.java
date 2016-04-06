package edu.scripps.yates.shared.model.interfaces;

import java.util.List;

import edu.scripps.yates.shared.model.RatioBean;
import edu.scripps.yates.shared.model.ScoreBean;

public interface ContainsRatios {
	/**
	 * Gets the ratios available between these two conditions. No matter the
	 * order of the comparison. It assures that not repeated ratios are in the
	 * result.
	 *
	 * @param conditionName
	 * @param condition2Name
	 * @param projectTag
	 * @param ratioName
	 *            if null, all ratios will be returned. If some value here, only
	 *            the ratios with that name will be returned
	 * @param skipInfinities
	 *            if true, the ratios with values equals to +INFINITY or
	 *            -INFINITY will not be returned
	 * @return
	 */
	public List<RatioBean> getRatiosByConditions(String conditionName,
			String condition2Name, String projectTag, String ratioName,
			boolean skipInfinities);

	/**
	 * Gets the scores associated to the ratios available between these two
	 * conditions.
	 *
	 * @param conditionName
	 * @param condition2Name
	 * @param projectTag
	 * @param ratioName
	 *            if null, all ratios will be returned. If some value here, only
	 *            the ratios with that name will be returned
	 * @param skipInfinities
	 *            if true, the ratios with values equals to +INFINITY or
	 *            -INFINITY will not be returned
	 * @return
	 */
	public List<ScoreBean> getRatioScoresByConditions(String conditionName,
			String condition2Name, String projectTag, String ratioName);

	/**
	 * Gets a string with the scores associated with the ratios
	 *
	 * @param condition1Name
	 * @param condition2Name
	 * @param projectTag
	 * @param ratioName
	 * @param skipInfinities
	 * @return
	 */
	public String getRatioScoreStringByConditions(String condition1Name,
			String condition2Name, String projectTag, String ratioName,
			boolean skipInfinities);

	/**
	 * Gets a string with the ratio values
	 * 
	 * @param condition1Name
	 * @param condition2Name
	 * @param projectTag
	 * @param ratioName
	 * @param skipInfinities
	 * @return
	 */
	public String getRatioStringByConditions(String condition1Name,
			String condition2Name, String projectTag, String ratioName,
			boolean skipInfinities);
}
