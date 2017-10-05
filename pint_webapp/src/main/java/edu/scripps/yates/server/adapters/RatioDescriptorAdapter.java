package edu.scripps.yates.server.adapters;

import edu.scripps.yates.proteindb.persistence.mysql.PeptideRatioValue;
import edu.scripps.yates.proteindb.persistence.mysql.ProteinRatioValue;
import edu.scripps.yates.proteindb.persistence.mysql.PsmRatioValue;
import edu.scripps.yates.proteindb.persistence.mysql.RatioDescriptor;
import edu.scripps.yates.proteindb.persistence.mysql.adapter.Adapter;
import edu.scripps.yates.shared.model.RatioDescriptorBean;
import edu.scripps.yates.shared.model.SharedAggregationLevel;

public class RatioDescriptorAdapter implements Adapter<RatioDescriptorBean> {
	private final RatioDescriptor descriptor;
	private final SharedAggregationLevel aggregationLevel;

	public RatioDescriptorAdapter(RatioDescriptor descriptor, SharedAggregationLevel aggregationLevel) {
		this.descriptor = descriptor;
		this.aggregationLevel = aggregationLevel;
	}

	@Override
	public RatioDescriptorBean adapt() {
		RatioDescriptorBean ret = new RatioDescriptorBean();
		ret.setCondition1Name(descriptor.getConditionByExperimentalCondition1Id().getName());
		ret.setCondition2Name(descriptor.getConditionByExperimentalCondition2Id().getName());
		ret.setProjectTag(descriptor.getConditionByExperimentalCondition1Id().getProject().getTag());
		ret.setRatioName(descriptor.getDescription());

		if (descriptor.getProteinRatioValues() != null && !descriptor.getProteinRatioValues().isEmpty()) {
			ProteinRatioValue proteinRatioValue = (ProteinRatioValue) descriptor.getProteinRatioValues().iterator()
					.next();
			if (proteinRatioValue.getConfidenceScoreName() != null) {
				ret.setProteinScoreName(proteinRatioValue.getConfidenceScoreName());
			}
		}
		if (descriptor.getPeptideRatioValues() != null && !descriptor.getPeptideRatioValues().isEmpty()) {
			PeptideRatioValue peptideRatioValue = (PeptideRatioValue) descriptor.getPeptideRatioValues().iterator()
					.next();
			if (peptideRatioValue.getConfidenceScoreName() != null) {
				ret.setPeptideScoreName(peptideRatioValue.getConfidenceScoreName());
			}
		}
		if (descriptor.getPsmRatioValues() != null && !descriptor.getPsmRatioValues().isEmpty()) {
			PsmRatioValue psmRatioValue = (PsmRatioValue) descriptor.getPsmRatioValues().iterator().next();
			if (psmRatioValue.getConfidenceScoreName() != null) {
				ret.setPsmScoreName(psmRatioValue.getConfidenceScoreName());
			}
		}
		ret.setAggregationLevel(aggregationLevel);
		return ret;
	}

}
