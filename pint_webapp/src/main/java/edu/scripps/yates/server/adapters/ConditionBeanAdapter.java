package edu.scripps.yates.server.adapters;

import edu.scripps.yates.proteindb.persistence.mysql.Condition;
import edu.scripps.yates.proteindb.persistence.mysql.adapter.Adapter;
import edu.scripps.yates.shared.model.ExperimentalConditionBean;
import edu.scripps.yates.shared.model.ProjectBean;
import gnu.trove.map.hash.TIntObjectHashMap;

public class ConditionBeanAdapter implements Adapter<ExperimentalConditionBean> {
	private final Condition experimentalCondition;
	private static ThreadLocal<TIntObjectHashMap<ExperimentalConditionBean>> map = new ThreadLocal<TIntObjectHashMap<ExperimentalConditionBean>>();

	public ConditionBeanAdapter(Condition experimentalCondition) {
		this.experimentalCondition = experimentalCondition;
		initializeMap();
	}

	private void initializeMap() {
		if (map.get() == null) {
			map.set(new TIntObjectHashMap<ExperimentalConditionBean>());
		}
	}

	@Override
	public ExperimentalConditionBean adapt() {
		if (map.get().containsKey(experimentalCondition.getId()))
			return map.get().get(experimentalCondition.getId());
		ExperimentalConditionBean ret = new ExperimentalConditionBean();
		map.get().put(experimentalCondition.getId(), ret);
		ret.setDescription(experimentalCondition.getDescription());
		ret.setName(experimentalCondition.getName());
		final ProjectBean project = new ProjectBeanAdapter(experimentalCondition.getProject()).adapt();
		ret.setProject(project);
		ret.setSample(new SampleBeanAdapter(experimentalCondition.getSample(), project).adapt());
		ret.setUnit(experimentalCondition.getUnit());
		ret.setValue(experimentalCondition.getValue());
		return ret;
	}

	public static void clearStaticMap() {
		if (map.get() != null) {
			map.get().clear();
		}
	}
}
