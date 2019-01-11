package edu.scripps.yates.server.adapters;

import edu.scripps.yates.proteindb.persistence.mysql.PeptideRatioValue;
import edu.scripps.yates.proteindb.persistence.mysql.adapter.Adapter;
import edu.scripps.yates.proteindb.persistence.mysql.utils.PersistenceUtils;
import edu.scripps.yates.shared.model.RatioBean;
import edu.scripps.yates.shared.model.RatioDescriptorBean;
import edu.scripps.yates.shared.model.SharedAggregationLevel;
import gnu.trove.map.hash.TIntObjectHashMap;

public class PeptideRatioBeanAdapter implements Adapter<RatioBean> {
	private final PeptideRatioValue peptideRatioValue;
	private final static ThreadLocal<TIntObjectHashMap<RatioBean>> map = new ThreadLocal<TIntObjectHashMap<RatioBean>>();

	public PeptideRatioBeanAdapter(PeptideRatioValue peptideRatioValue) {
		this.peptideRatioValue = peptideRatioValue;
		initializeMap();
	}

	private static void initializeMap() {
		if (map.get() == null) {
			map.set(new TIntObjectHashMap<RatioBean>());
		}
	}

	@Override
	public RatioBean adapt() {
		if (map.get().containsKey(peptideRatioValue.getId()))
			return map.get().get(peptideRatioValue.getId());
		final RatioBean ret = new RatioBean();
		map.get().put(peptideRatioValue.getId(), ret);
		if (peptideRatioValue.getConfidenceScoreValue() != null) {
			ret.setAssociatedConfidenceScore(
					new ScoreBeanAdapter(String.valueOf(peptideRatioValue.getConfidenceScoreValue()),
							peptideRatioValue.getConfidenceScoreName(), peptideRatioValue.getConfidenceScoreType())
									.adapt());
		}
		ret.setCondition1(new ConditionBeanAdapter(
				peptideRatioValue.getRatioDescriptor().getConditionByExperimentalCondition1Id(), true).adapt());
		ret.setCondition2(new ConditionBeanAdapter(
				peptideRatioValue.getRatioDescriptor().getConditionByExperimentalCondition2Id(), true).adapt());
		ret.setDescription(peptideRatioValue.getRatioDescriptor().getDescription());
		ret.setValue(PersistenceUtils.parseRatioValueConvert2Infinities(peptideRatioValue.getValue()));
		ret.setDbID(peptideRatioValue.getId());
		final RatioDescriptorBean ratioDescriptorBean = new RatioDescriptorAdapter(
				peptideRatioValue.getRatioDescriptor(), SharedAggregationLevel.PEPTIDE).adapt();
		ret.setRatioDescriptorBean(ratioDescriptorBean);
		return ret;
	}

	public static void clearStaticMap() {
		if (map.get() != null) {
			map.get().clear();
		}
	}

	public static RatioBean getBeanByPeptideRatioValueID(Integer id) {
		initializeMap();
		return map.get().get(id);
	}
}
