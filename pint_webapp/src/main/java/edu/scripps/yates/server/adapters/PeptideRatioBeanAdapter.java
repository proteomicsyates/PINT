package edu.scripps.yates.server.adapters;

import java.util.HashMap;
import java.util.Map;

import edu.scripps.yates.proteindb.persistence.mysql.PeptideRatioValue;
import edu.scripps.yates.proteindb.persistence.mysql.adapter.Adapter;
import edu.scripps.yates.proteindb.persistence.mysql.utils.PersistenceUtils;
import edu.scripps.yates.shared.model.RatioBean;
import edu.scripps.yates.shared.model.RatioDescriptorBean;
import edu.scripps.yates.shared.model.SharedAggregationLevel;

public class PeptideRatioBeanAdapter implements Adapter<RatioBean> {
	private final PeptideRatioValue peptideRatioValue;
	private final static Map<Integer, RatioBean> map = new HashMap<Integer, RatioBean>();

	public PeptideRatioBeanAdapter(PeptideRatioValue peptideRatioValue) {
		this.peptideRatioValue = peptideRatioValue;
	}

	@Override
	public RatioBean adapt() {
		if (map.containsKey(peptideRatioValue.getId()))
			return map.get(peptideRatioValue.getId());
		RatioBean ret = new RatioBean();
		map.put(peptideRatioValue.getId(), ret);
		if (peptideRatioValue.getConfidenceScoreValue() != null) {
			ret.setAssociatedConfidenceScore(
					new ScoreBeanAdapter(String.valueOf(peptideRatioValue.getConfidenceScoreValue()),
							peptideRatioValue.getConfidenceScoreName(), peptideRatioValue.getConfidenceScoreType())
									.adapt());
		}
		ret.setCondition1(new ConditionBeanAdapter(
				peptideRatioValue.getRatioDescriptor().getConditionByExperimentalCondition1Id()).adapt());
		ret.setCondition2(new ConditionBeanAdapter(
				peptideRatioValue.getRatioDescriptor().getConditionByExperimentalCondition2Id()).adapt());
		ret.setDescription(peptideRatioValue.getRatioDescriptor().getDescription());
		ret.setValue(PersistenceUtils.parseRatioValueConvert2Infinities(peptideRatioValue.getValue()));
		ret.setDbID(peptideRatioValue.getId());
		RatioDescriptorBean ratioDescriptorBean = new RatioDescriptorAdapter(peptideRatioValue.getRatioDescriptor(),
				SharedAggregationLevel.PEPTIDE).adapt();
		ret.setRatioDescriptorBean(ratioDescriptorBean);
		return ret;
	}
}
