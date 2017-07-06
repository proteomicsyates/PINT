package edu.scripps.yates.server.adapters;

import edu.scripps.yates.proteindb.persistence.mysql.ProteinThreshold;
import edu.scripps.yates.proteindb.persistence.mysql.adapter.Adapter;
import edu.scripps.yates.shared.model.ThresholdBean;
import gnu.trove.map.hash.TIntObjectHashMap;

public class ThresholdBeanAdapter implements Adapter<ThresholdBean> {
	private final ProteinThreshold appliedThreshold;
	private final static TIntObjectHashMap<ThresholdBean> map = new TIntObjectHashMap<ThresholdBean>();

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
