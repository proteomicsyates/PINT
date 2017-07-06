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
	private final static TIntObjectHashMap<RatioBean> map = new TIntObjectHashMap<RatioBean>();

	public ProteinRatioBeanAdapter(ProteinRatioValue proteinRatioValue) {
		this.proteinRatioValue = proteinRatioValue;
	}

	@Override
	public RatioBean adapt() {
		if (map.containsKey(proteinRatioValue.getId()))
			return map.get(proteinRatioValue.getId());
		RatioBean ret = new RatioBean();
		map.put(proteinRatioValue.getId(), ret);
		if (proteinRatioValue.getConfidenceScoreValue() != null) {
			ret.setAssociatedConfidenceScore(
					new ScoreBeanAdapter(String.valueOf(proteinRatioValue.getConfidenceScoreValue()),
							proteinRatioValue.getConfidenceScoreName(), proteinRatioValue.getConfidenceScoreType())
									.adapt());
		}
		ret.setCondition1(new ConditionBeanAdapter(
				proteinRatioValue.getRatioDescriptor().getConditionByExperimentalCondition1Id()).adapt());
		ret.setCondition2(new ConditionBeanAdapter(
				proteinRatioValue.getRatioDescriptor().getConditionByExperimentalCondition2Id()).adapt());
		ret.setDescription(proteinRatioValue.getRatioDescriptor().getDescription());
		ret.setValue(PersistenceUtils.parseRatioValueConvert2Infinities(proteinRatioValue.getValue()));
		ret.setDbID(proteinRatioValue.getId());
		RatioDescriptorBean ratioDescriptorBean = new RatioDescriptorAdapter(proteinRatioValue.getRatioDescriptor(),
				SharedAggregationLevel.PROTEIN).adapt();
		ret.setRatioDescriptorBean(ratioDescriptorBean);
		return ret;
	}

}
