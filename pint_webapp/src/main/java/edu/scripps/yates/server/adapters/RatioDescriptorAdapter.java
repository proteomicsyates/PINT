package edu.scripps.yates.server.adapters;

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
		ret.setAggregationLevel(aggregationLevel);
		return ret;
	}

}
