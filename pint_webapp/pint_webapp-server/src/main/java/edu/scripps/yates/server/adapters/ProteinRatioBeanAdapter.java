package edu.scripps.yates.server.adapters;

import edu.scripps.yates.proteindb.persistence.mysql.ProteinRatioValue;
import edu.scripps.yates.proteindb.persistence.mysql.adapter.Adapter;
import edu.scripps.yates.proteindb.persistence.mysql.utils.PersistenceUtils;
import edu.scripps.yates.shared.model.RatioBean;
import edu.scripps.yates.shared.model.RatioDescriptorBean;
import edu.scripps.yates.shared.model.SharedAggregationLevel;
import gnu.trove.map.hash.TIntObjectHashMap;

public class ProteinRatioBeanAdapter implements Adapter<RatioBean> {
	private final ProteinRatioValue proteinRatioValue;
	private final static ThreadLocal<TIntObjectHashMap<RatioBean>> map = new ThreadLocal<TIntObjectHashMap<RatioBean>>();

	public ProteinRatioBeanAdapter(ProteinRatioValue proteinRatioValue) {
		this.proteinRatioValue = proteinRatioValue;
		initializeMap();
	}

	private static void initializeMap() {
		if (map.get() == null) {
			map.set(new TIntObjectHashMap<RatioBean>());
		}
	}

	@Override
	public RatioBean adapt() {
		if (map.get().containsKey(proteinRatioValue.getId()))
			return map.get().get(proteinRatioValue.getId());
		final RatioBean ret = new RatioBean();
		map.get().put(proteinRatioValue.getId(), ret);
		if (proteinRatioValue.getConfidenceScoreValue() != null) {
			ret.setAssociatedConfidenceScore(
					new ScoreBeanAdapter(String.valueOf(proteinRatioValue.getConfidenceScoreValue()),
							proteinRatioValue.getConfidenceScoreName(), proteinRatioValue.getConfidenceScoreType())
									.adapt());
		}
		ret.setCondition1(new ConditionBeanAdapter(
				proteinRatioValue.getRatioDescriptor().getConditionByExperimentalCondition1Id(), true).adapt());
		ret.setCondition2(new ConditionBeanAdapter(
				proteinRatioValue.getRatioDescriptor().getConditionByExperimentalCondition2Id(), true).adapt());
		ret.setDescription(proteinRatioValue.getRatioDescriptor().getDescription());
		ret.setValue(PersistenceUtils.parseRatioValueConvert2Infinities(proteinRatioValue.getValue()));
		ret.setDbID(proteinRatioValue.getId());
		final RatioDescriptorBean ratioDescriptorBean = new RatioDescriptorAdapter(
				proteinRatioValue.getRatioDescriptor(), SharedAggregationLevel.PROTEIN).adapt();
		ret.setRatioDescriptorBean(ratioDescriptorBean);
		return ret;
	}

	public static void clearStaticMap() {
		if (map.get() != null) {
			map.get().clear();
		}
	}

	public static RatioBean getBeanByProteinRatioValueID(int id) {
		initializeMap();
		return map.get().get(id);
	}
}
