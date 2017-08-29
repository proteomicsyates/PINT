package edu.scripps.yates.server.adapters;

import edu.scripps.yates.proteindb.persistence.mysql.MsRun;
import edu.scripps.yates.proteindb.persistence.mysql.adapter.Adapter;
import edu.scripps.yates.shared.model.MSRunBean;
import gnu.trove.map.hash.TIntObjectHashMap;

public class MSRunBeanAdapter implements Adapter<MSRunBean> {
	private final MsRun msRun;
	private final static ThreadLocal<TIntObjectHashMap<MSRunBean>> map = new ThreadLocal<TIntObjectHashMap<MSRunBean>>();

	public MSRunBeanAdapter(MsRun msRun) {
		this.msRun = msRun;
		initializeMap();
	}

	private void initializeMap() {
		if (map.get() == null) {
			map.set(new TIntObjectHashMap<MSRunBean>());
		}
	}

	@Override
	public MSRunBean adapt() {
		if (map.get().containsKey(msRun.getId()))
			return map.get().get(msRun.getId());
		MSRunBean ret = new MSRunBean();
		map.get().put(msRun.getId(), ret);
		ret.setDate(msRun.getDate());
		ret.setPath(msRun.getPath());
		ret.setRunID(msRun.getRunId());
		ret.setDbID(msRun.getId());
		ret.setProject(new ProjectBeanAdapter(msRun.getProject()).adapt());
		return ret;
	}

	public static void clearStaticMap() {
		if (map.get() != null) {
			map.get().clear();
		}
	}
}
