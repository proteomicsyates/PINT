package edu.scripps.yates.server.adapters;

import edu.scripps.yates.proteindb.persistence.mysql.adapter.Adapter;
import edu.scripps.yates.shared.model.LabelBean;
import gnu.trove.map.hash.TIntObjectHashMap;

public class LabelBeanAdapter implements Adapter<LabelBean> {
	private final edu.scripps.yates.proteindb.persistence.mysql.Label label;
	private final static ThreadLocal<TIntObjectHashMap<LabelBean>> map = new ThreadLocal<TIntObjectHashMap<LabelBean>>();

	public LabelBeanAdapter(edu.scripps.yates.proteindb.persistence.mysql.Label label) {
		this.label = label;
		initializeMap();
	}

	private void initializeMap() {
		if (map.get() == null) {
			map.set(new TIntObjectHashMap<LabelBean>());
		}
	}

	@Override
	public LabelBean adapt() {
		if (map.get().containsKey(label.getId()))
			return map.get().get(label.getId());
		LabelBean ret = new LabelBean();
		map.get().put(label.getId(), ret);
		ret.setMassDiff(label.getMassDiff());
		ret.setName(label.getName());
		return ret;
	}

	public static void clearStaticMap() {
		if (map.get() != null) {
			map.get().clear();
		}
	}
}
