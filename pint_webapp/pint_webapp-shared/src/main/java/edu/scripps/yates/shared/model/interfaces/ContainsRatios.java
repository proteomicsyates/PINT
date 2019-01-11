package edu.scripps.yates.shared.model.interfaces;

import java.util.List;
import java.util.Map;
import java.util.Set;

import edu.scripps.yates.shared.model.RatioBean;
import edu.scripps.yates.shared.model.RatioDistribution;
import edu.scripps.yates.shared.model.ScoreBean;

public interface ContainsRatios {
	/**
	 * Gets the ratios available between these two conditions. No matter the order
	 * of the comparison. It assures that not repeated ratios are in the result.
	 *
	 * @param conditionName
	 * @param condition2Name
	 * @param projectTag
	 * @param ratioName      if null, all ratios will be returned. If some value
	 *                       here, only the ratios with that name will be returned
	 * @param skipInfinities if true, the ratios with values equals to +INFINITY or
	 *                       -INFINITY will not be returned
	 * @return
	 */
	public List<RatioBean> getRatiosByConditions(String conditionName, String condition2Name, String projectTag,
			String ratioName, boolean skipInfinities);

	/**
	 * Gets the scores associated to the ratios available between these two
	 * conditions.
	 *
	 * @param conditionName
	 * @param condition2Name
	 * @param projectTag
	 * @param ratioName      if null, all ratios will be returned. If some value
	 *                       here, only the ratios with that name will be returned
	 * @param ratioScore
	 * @param skipInfinities if true, the ratios with values equals to +INFINITY or
	 *                       -INFINITY will not be returned
	 * @return
	 */
	public List<ScoreBean> getRatioScoresByConditions(String conditionName, String condition2Name, String projectTag,
			String ratioName, String ratioScore);

	public Map<String, RatioDistribution> getRatioDistributions();

	public void addRatioDistribution(RatioDistribution ratioDistribution);

	public RatioDistribution getRatioDistribution(RatioBean ratio);

	public Set<RatioBean> getRatios();

}
