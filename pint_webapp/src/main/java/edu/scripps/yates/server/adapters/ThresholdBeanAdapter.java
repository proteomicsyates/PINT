package edu.scripps.yates.server.adapters;

import edu.scripps.yates.proteindb.persistence.mysql.ProteinThreshold;
import edu.scripps.yates.proteindb.persistence.mysql.adapter.Adapter;
import edu.scripps.yates.shared.model.ThresholdBean;
import gnu.trove.map.hash.TIntObjectHashMap;

public class ThresholdBeanAdapter implements Adapter<ThresholdBean> {
	private final ProteinThreshold appliedThreshold;
	private final static ThreadLocal<TIntObjectHashMap<ThresholdBean>> map = new ThreadLocal<TIntObjectHashMap<ThresholdBean>>();

	public ThresholdBeanAdapter(ProteinThreshold appliedThreshold) {
		this.appliedThreshold = appliedThreshold;
		initializeMap();
	}

	private void initializeMap() {
		if (map.get() == null) {
			map.set(new TIntObjectHashMap<ThresholdBean>());
		}
	}

	@Override
	public ThresholdBean adapt() {
		if (map.get().containsKey(appliedThreshold.getId())) {
			map.get().get(appliedThreshold.getId());
		}
		ThresholdBean ret = new ThresholdBean();
		map.get().put(appliedThreshold.getId(), ret);
		ret.setDescription(appliedThreshold.getThreshold().getDescription());
		ret.setName(appliedThreshold.getThreshold().getName());
		ret.setPass(appliedThreshold.isPassThreshold());
		return ret;
	}

	public static void clearStaticMap() {
		if (map.get() != null) {
			map.get().clear();
		}
	}
}
