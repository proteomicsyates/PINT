package edu.scripps.yates.server.adapters;

import java.util.HashMap;
import java.util.Map;

import edu.scripps.yates.proteindb.persistence.mysql.ProteinThreshold;
import edu.scripps.yates.proteindb.persistence.mysql.adapter.Adapter;
import edu.scripps.yates.shared.model.ThresholdBean;

public class ThresholdBeanAdapter implements Adapter<ThresholdBean> {
	private final ProteinThreshold appliedThreshold;
	private final static Map<Integer, ThresholdBean> map = new HashMap<Integer, ThresholdBean>();

	public ThresholdBeanAdapter(ProteinThreshold appliedThreshold) {
		this.appliedThreshold = appliedThreshold;
	}

	@Override
	public ThresholdBean adapt() {
		if (map.containsKey(appliedThreshold.getId())) {
			map.get(appliedThreshold.getId());
		}
		ThresholdBean ret = new ThresholdBean();
		map.put(appliedThreshold.getId(), ret);
		ret.setDescription(appliedThreshold.getThreshold().getDescription());
		ret.setName(appliedThreshold.getThreshold().getName());
		ret.setPass(appliedThreshold.isPassThreshold());
		return ret;
	}

}
