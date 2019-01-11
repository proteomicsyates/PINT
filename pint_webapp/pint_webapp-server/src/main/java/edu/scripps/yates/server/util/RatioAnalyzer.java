package edu.scripps.yates.server.util;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

import org.apache.log4j.Logger;

import edu.scripps.yates.proteindb.persistence.mysql.access.PreparedCriteria;
import edu.scripps.yates.shared.model.RatioBean;
import edu.scripps.yates.shared.model.RatioDistribution;
import edu.scripps.yates.shared.model.SharedAggregationLevel;
import edu.scripps.yates.shared.util.SharedDataUtil;
import gnu.trove.map.hash.THashMap;

public class RatioAnalyzer {
	private final static Logger log = Logger.getLogger(RatioAnalyzer.class);
	private static final Map<String, RatioDistribution> ratioDistributionsByRatio = new THashMap<String, RatioDistribution>();

	private RatioDistribution getRatioDistribution(String key, RatioBean ratio) {
		log.info("Creating a new ratioDistribution for " + key);
		RatioDistribution ratioDistribution = new RatioDistribution();
		ratioDistributionsByRatio.put(key, ratioDistribution);
		ratioDistribution.setRatioKey(key);
		double max = ratio.getValue();
		double min = ratio.getValue();
		// query the database for the max and min
		final SharedAggregationLevel aggregationLevel = ratio.getRatioDescriptorBean().getAggregationLevel();
		switch (aggregationLevel) {
		case PROTEIN:
			final Object uniqueResult = PreparedCriteria
					.getCriteriaForProteinRatioMaximumValue(ratio.getCondition1().getId(),
							ratio.getCondition2().getId(), ratio.getCondition1().getProject().getTag(), ratio.getId())
					.uniqueResult();
			if (uniqueResult != null) {
				max = (Double) uniqueResult;
				log.info("max= " + min);
			}
			final Object uniqueResult2 = PreparedCriteria
					.getCriteriaForProteinRatioMinimumValue(ratio.getCondition1().getId(),
							ratio.getCondition2().getId(), ratio.getCondition1().getProject().getTag(), ratio.getId())
					.uniqueResult();
			if (uniqueResult2 != null) {
				min = (Double) uniqueResult2;
				log.info("min= " + min);
			}
			break;
		case PEPTIDE:
			final Object uniqueResult3 = PreparedCriteria
					.getCriteriaForPeptideRatioMaximumValue(ratio.getCondition1().getId(),
							ratio.getCondition2().getId(), ratio.getCondition1().getProject().getTag(), ratio.getId())
					.uniqueResult();
			if (uniqueResult3 != null) {
				max = (Double) uniqueResult3;
				log.info("max= " + min);
			}
			final Object uniqueResult4 = PreparedCriteria
					.getCriteriaForPeptideRatioMinimumValue(ratio.getCondition1().getId(),
							ratio.getCondition2().getId(), ratio.getCondition1().getProject().getTag(), ratio.getId())
					.uniqueResult();
			if (uniqueResult4 != null) {
				min = (Double) uniqueResult4;
				log.info("min= " + min);
			}
			break;
		case PSM:
			final Object uniqueResult5 = PreparedCriteria
					.getCriteriaForPsmRatioMaximumValue(ratio.getCondition1().getId(), ratio.getCondition2().getId(),
							ratio.getCondition1().getProject().getTag(), ratio.getId())
					.uniqueResult();
			if (uniqueResult5 != null) {
				max = (Double) uniqueResult5;
				log.info("max= " + min);

			}
			final Object uniqueResult6 = PreparedCriteria
					.getCriteriaForPsmRatioMinimumValue(ratio.getCondition1().getId(), ratio.getCondition2().getId(),
							ratio.getCondition1().getProject().getTag(), ratio.getId())
					.uniqueResult();
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
		return ratioDistribution;
	}

	public void addRatio(RatioBean ratio) {
		String key = SharedDataUtil.getRatioKey(ratio);
		if (!ratioDistributionsByRatio.containsKey(key)) {
			CompletableFuture.supplyAsync(() -> getRatioDistribution(key, ratio))
					.thenAccept(this::storeRatioDistribution).thenRun(() -> updateRatioDistribution(ratio));
		} else {
			updateRatioDistribution(ratio);
		}

	}

	private synchronized void updateRatioDistribution(RatioBean ratio) {

		String key = SharedDataUtil.getRatioKey(ratio);
		if (!Double.isInfinite(ratio.getValue())) {
			final RatioDistribution ratioDistribution = ratioDistributionsByRatio.get(key);
			if (Double.compare(ratioDistribution.getMaxRatio(), ratio.getValue()) < 0) {
				log.info("Setting maximum to " + ratio.getValue() + " in ratio " + key);
				ratioDistribution.setMaxRatio(ratio.getValue());
			}
			if (Double.compare(ratioDistribution.getMinRatio(), ratio.getValue()) > 0) {
				log.info("Setting minimum to " + ratio.getValue() + " in ratio " + key);
				ratioDistribution.setMinRatio(ratio.getValue());
			}
		}

	}

	private void storeRatioDistribution(RatioDistribution ratioDistribution) {
		log.info("Adding " + ratioDistribution + " ratio distribution");
		ratioDistributionsByRatio.put(ratioDistribution.getRatioKey(), ratioDistribution);
	}

	public RatioDistribution getRatioDistribution(RatioBean ratio) {
		return ratioDistributionsByRatio.get(SharedDataUtil.getRatioKey(ratio));
	}

	public void addRatios(Collection<RatioBean> ratios) {
		for (RatioBean ratioBean : ratios) {
			addRatio(ratioBean);
		}
	}

}
