package edu.scripps.yates.server.adapters;

import java.util.Map;

import edu.scripps.yates.proteindb.persistence.mysql.Tissue;
import edu.scripps.yates.proteindb.persistence.mysql.adapter.Adapter;
import edu.scripps.yates.shared.model.TissueBean;
import gnu.trove.map.hash.THashMap;

public class TissueBeanAdapter implements Adapter<TissueBean> {
	private final Tissue tissue;
	private final static ThreadLocal<Map<String, TissueBean>> map = new ThreadLocal<Map<String, TissueBean>>();

	public TissueBeanAdapter(Tissue tissue) {
		this.tissue = tissue;
		initializeMap();
	}

	private void initializeMap() {
		if (map.get() == null) {
			map.set(new THashMap<String, TissueBean>());
		}
	}

	@Override
	public TissueBean adapt() {
		if (map.get().containsKey(tissue.getTissueId()))
			map.get().get(tissue.getTissueId());
		TissueBean ret = new TissueBean();
		ret.setDescription(tissue.getName());
		ret.setTissueID(tissue.getTissueId());
		map.get().put(tissue.getTissueId(), ret);
		return ret;
	}

	public static void clearStaticMap() {
		if (map.get() != null) {
			map.get().clear();
		}
	}
}
