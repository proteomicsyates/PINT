package edu.scripps.yates.server.util;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import edu.scripps.yates.shared.model.RatioBean;
import edu.scripps.yates.shared.model.RatioDistribution;
import edu.scripps.yates.shared.util.SharedDataUtils;

public class RatioAnalyzer {
	private final Map<String, RatioDistribution> ratioDistributionsByRatio = new HashMap<String, RatioDistribution>();

	public void addRatio(RatioBean ratio) {
		String key = SharedDataUtils.getRatioKey(ratio);
		if (!ratioDistributionsByRatio.containsKey(key)) {
			RatioDistribution ratioDistribution = new RatioDistribution();
			ratioDistribution.setMaxRatio(ratio.getValue());
			ratioDistribution.setMinRatio(ratio.getValue());
			ratioDistribution.setRatioKey(key);
			ratioDistributionsByRatio.put(key, ratioDistribution);
		} else {
			final RatioDistribution ratioDistribution = ratioDistributionsByRatio.get(key);
			if (Double.compare(ratioDistribution.getMaxRatio(), ratio.getValue()) < 0) {
				ratioDistribution.setMaxRatio(ratio.getValue());
			}
			if (Double.compare(ratioDistribution.getMinRatio(), ratio.getValue()) > 0) {
				ratioDistribution.setMinRatio(ratio.getValue());
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
