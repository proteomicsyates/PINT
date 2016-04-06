package edu.scripps.yates.server.adapters;

import java.util.HashMap;
import java.util.Map;

import edu.scripps.yates.proteindb.persistence.mysql.Condition;
import edu.scripps.yates.proteindb.persistence.mysql.adapter.Adapter;
import edu.scripps.yates.shared.model.ExperimentalConditionBean;

public class ConditionBeanAdapter implements
		Adapter<ExperimentalConditionBean> {
	private final Condition experimentalCondition;
	private static Map<Integer, ExperimentalConditionBean> map = new HashMap<Integer, ExperimentalConditionBean>();

	public ConditionBeanAdapter(Condition experimentalCondition) {
		this.experimentalCondition = experimentalCondition;
	}

	@Override
	public ExperimentalConditionBean adapt() {
		if (map.containsKey(experimentalCondition.getId()))
			return map.get(experimentalCondition.getId());
		ExperimentalConditionBean ret = new ExperimentalConditionBean();
		map.put(experimentalCondition.getId(), ret);
		ret.setDescription(experimentalCondition.getDescription());
		ret.setName(experimentalCondition.getName());
		ret.setProject(new ProjectBeanAdapter(experimentalCondition
				.getProject()).adapt());
		ret.setSample(new SampleBeanAdapter(experimentalCondition.getSample())
				.adapt());
		ret.setUnit(experimentalCondition.getUnit());
		ret.setValue(experimentalCondition.getValue());
		return ret;
	}

}
