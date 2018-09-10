package edu.scripps.yates.server.adapters;

import edu.scripps.yates.proteindb.persistence.mysql.MsRun;
import edu.scripps.yates.proteindb.persistence.mysql.adapter.Adapter;
import edu.scripps.yates.proteindb.persistence.mysql.utils.tablemapper.MSRunToPeptideTableMapper;
import edu.scripps.yates.proteindb.persistence.mysql.utils.tablemapper.MSRunToProteinTableMapper;
import edu.scripps.yates.shared.model.MSRunBean;
import gnu.trove.map.hash.TIntObjectHashMap;

public class MSRunBeanAdapter implements Adapter<MSRunBean> {
	private final MsRun msRun;
	private final boolean mapTables;
	private final static ThreadLocal<TIntObjectHashMap<MSRunBean>> map = new ThreadLocal<TIntObjectHashMap<MSRunBean>>();

	public MSRunBeanAdapter(MsRun msRun, boolean mapTables) {
		this.msRun = msRun;
		this.mapTables = mapTables;
		initializeMap();
	}

	private static void initializeMap() {
		if (map.get() == null) {
			map.set(new TIntObjectHashMap<MSRunBean>());
		}
	}

	@Override
	public MSRunBean adapt() {
		if (map.get().containsKey(msRun.getId())) {
			if (mapTables) {
				MSRunToProteinTableMapper.getInstance().addObject1(msRun);
				MSRunToPeptideTableMapper.getInstance().addObject1(msRun);
			}
			return map.get().get(msRun.getId());
		}
		final MSRunBean ret = new MSRunBean();
		map.get().put(msRun.getId(), ret);
		ret.setDate(msRun.getDate());
		ret.setPath(msRun.getPath());
		ret.setRunID(msRun.getRunId());
		ret.setDbID(msRun.getId());
		ret.setProject(new ProjectBeanAdapter(msRun.getProject(), false).adapt());
		if (mapTables) {
			MSRunToProteinTableMapper.getInstance().addObject1(msRun);
			MSRunToPeptideTableMapper.getInstance().addObject1(msRun);
		}
		return ret;
	}

	public static void clearStaticMap() {
		if (map.get() != null) {
			map.get().clear();
		}
	}

	public static MSRunBean getBeanByMSRunID(int msRunID) {
		initializeMap();
		return map.get().get(msRunID);
	}
}
