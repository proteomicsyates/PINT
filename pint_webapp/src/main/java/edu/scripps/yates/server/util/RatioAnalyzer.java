package edu.scripps.yates.server.util;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import edu.scripps.yates.proteindb.persistence.mysql.access.PreparedCriteria;
import edu.scripps.yates.shared.model.RatioBean;
import edu.scripps.yates.shared.model.RatioDistribution;
import edu.scripps.yates.shared.model.SharedAggregationLevel;
import edu.scripps.yates.shared.util.SharedDataUtils;

public class RatioAnalyzer {
	private final static Logger log = Logger.getLogger(RatioAnalyzer.class);
	private final Map<String, RatioDistribution> ratioDistributionsByRatio = new HashMap<String, RatioDistribution>();

	public void addRatio(RatioBean ratio) {
		String key = SharedDataUtils.getRatioKey(ratio);
		if (!ratioDistributionsByRatio.containsKey(key)) {
			RatioDistribution ratioDistribution = new RatioDistribution();
			double max = ratio.getValue();
			double min = ratio.getValue();
			// query the database for the max and min
			final SharedAggregationLevel aggregationLevel = ratio.getRatioDescriptorBean().getAggregationLevel();
			switch (aggregationLevel) {
			case PROTEIN:
				final Object uniqueResult = PreparedCriteria.getCriteriaForProteinRatioMaximumValue(
						ratio.getCondition1().getId(), ratio.getCondition2().getId(),
						ratio.getCondition1().getProject().getTag(), ratio.getId()).uniqueResult();
				if (uniqueResult != null) {
					max = (Double) uniqueResult;
					log.info("max= " + min);
				}
				final Object uniqueResult2 = PreparedCriteria.getCriteriaForProteinRatioMinimumValue(
						ratio.getCondition1().getId(), ratio.getCondition2().getId(),
						ratio.getCondition1().getProject().getTag(), ratio.getId()).uniqueResult();
				if (uniqueResult2 != null) {
					min = (Double) uniqueResult2;
					log.info("min= " + min);
				}
				break;
			case PEPTIDE:
				final Object uniqueResult3 = PreparedCriteria.getCriteriaForPeptideRatioMaximumValue(
						ratio.getCondition1().getId(), ratio.getCondition2().getId(),
						ratio.getCondition1().getProject().getTag(), ratio.getId()).uniqueResult();
				if (uniqueResult3 != null) {
					max = (Double) uniqueResult3;
					log.info("max= " + min);
				}
				final Object uniqueResult4 = PreparedCriteria.getCriteriaForPeptideRatioMinimumValue(
						ratio.getCondition1().getId(), ratio.getCondition2().getId(),
						ratio.getCondition1().getProject().getTag(), ratio.getId()).uniqueResult();
				if (uniqueResult4 != null) {
					min = (Double) uniqueResult4;
					log.info("min= " + min);
				}
				break;
			case PSM:
				final Object uniqueResult5 = PreparedCriteria.getCriteriaForPsmRatioMaximumValue(
						ratio.getCondition1().getId(), ratio.getCondition2().getId(),
						ratio.getCondition1().getProject().getTag(), ratio.getId()).uniqueResult();
				if (uniqueResult5 != null) {
					max = (Double) uniqueResult5;
					log.info("max= " + min);

				}
				final Object uniqueResult6 = PreparedCriteria.getCriteriaForPsmRatioMinimumValue(
						ratio.getCondition1().getId(), ratio.getCondition2().getId(),
						ratio.getCondition1().getProject().getTag(), ratio.getId()).uniqueResult();
				if (uniqueResult6 != null) {
					min = (Double) uniqueResult6;
					log.info("min= " + min);
				}
				break;
			default:
				break;
			}
			ratioDistribution.setMaxRatio(max);
			ratioDistribution.setMinRatio(min);
			ratioDistribution.setRatioKey(key);
			ratioDistributionsByRatio.put(key, ratioDistribution);
		} else {
			if (!Double.isInfinite(ratio.getValue())) {
				final RatioDistribution ratioDistribution = ratioDistributionsByRatio.get(key);
				if (Double.compare(ratioDistribution.getMaxRatio(), ratio.getValue()) < 0) {
					ratioDistribution.setMaxRatio(ratio.getValue());
				}
				if (Double.compare(ratioDistribution.getMinRatio(), ratio.getValue()) > 0) {
					ratioDistribution.setMinRatio(ratio.getValue());
				}
			}
		}

	}

	public RatioDistribution getRatioDistribution(RatioBean ratio) {
		return ratioDistributionsByRatio.get(SharedDataUtils.getRatioKey(ratio));
	}

	public void addRatios(Collection<RatioBean> ratios) {
		for (RatioBean ratioBean : ratios) {
			addRatio(ratioBean);
		}
	}

	public void clear() {
		ratioDistributionsByRatio.clear();
	}
}
