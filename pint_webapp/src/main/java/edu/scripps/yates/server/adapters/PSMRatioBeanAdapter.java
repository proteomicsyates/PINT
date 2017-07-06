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
	private final static TIntObjectHashMap<RatioBean> map = new TIntObjectHashMap<RatioBean>();

	public PSMRatioBeanAdapter(PsmRatioValue peptideRatioValue) {
		psmRatioValue = peptideRatioValue;
	}

	@Override
	public RatioBean adapt() {
		if (map.containsKey(psmRatioValue.getId()))
			return map.get(psmRatioValue.getId());
		RatioBean ret = new RatioBean();
		map.put(psmRatioValue.getId(), ret);
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
}
