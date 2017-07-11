package edu.scripps.yates.server.adapters;

import edu.scripps.yates.proteindb.persistence.mysql.PsmRatioValue;
import edu.scripps.yates.proteindb.persistence.mysql.adapter.Adapter;
import edu.scripps.yates.proteindb.persistence.mysql.utils.PersistenceUtils;
import edu.scripps.yates.shared.model.RatioBean;
import edu.scripps.yates.shared.model.RatioDescriptorBean;
import edu.scripps.yates.shared.model.SharedAggregationLevel;
import gnu.trove.map.hash.TIntObjectHashMap;

public class PSMRatioBeanAdapter implements Adapter<RatioBean> {
	private final PsmRatioValue psmRatioValue;
	private final static ThreadLocal<TIntObjectHashMap<RatioBean>> map = new ThreadLocal<TIntObjectHashMap<RatioBean>>();

	public PSMRatioBeanAdapter(PsmRatioValue peptideRatioValue) {
		psmRatioValue = peptideRatioValue;
		initializeMap();
	}

	private void initializeMap() {
		if (map.get() == null) {
			map.set(new TIntObjectHashMap<RatioBean>());
		}
	}

	@Override
	public RatioBean adapt() {
		if (map.get().containsKey(psmRatioValue.getId()))
			return map.get().get(psmRatioValue.getId());
		RatioBean ret = new RatioBean();
		map.get().put(psmRatioValue.getId(), ret);
		if (psmRatioValue.getConfidenceScoreValue() != null) {
			ret.setAssociatedConfidenceScore(
					new ScoreBeanAdapter(String.valueOf(psmRatioValue.getConfidenceScoreValue()),
							psmRatioValue.getConfidenceScoreName(), psmRatioValue.getConfidenceScoreType()).adapt());
		}
		ret.setCondition1(
				new ConditionBeanAdapter(psmRatioValue.getRatioDescriptor().getConditionByExperimentalCondition1Id())
						.adapt());
		ret.setCondition2(
				new ConditionBeanAdapter(psmRatioValue.getRatioDescriptor().getConditionByExperimentalCondition2Id())
						.adapt());
		ret.setDescription(psmRatioValue.getRatioDescriptor().getDescription());
		ret.setValue(PersistenceUtils.parseRatioValueConvert2Infinities(psmRatioValue.getValue()));
		ret.setDbID(psmRatioValue.getId());
		RatioDescriptorBean ratioDescriptorBean = new RatioDescriptorAdapter(psmRatioValue.getRatioDescriptor(),
				SharedAggregationLevel.PSM).adapt();
		ret.setRatioDescriptorBean(ratioDescriptorBean);
		return ret;
	}

	public static void clearStaticMap() {
		if (map.get() != null) {
			map.get().clear();
		}
	}
}
