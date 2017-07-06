package edu.scripps.yates.server.adapters;

import edu.scripps.yates.proteindb.persistence.mysql.MsRun;
import edu.scripps.yates.proteindb.persistence.mysql.adapter.Adapter;
import edu.scripps.yates.shared.model.MSRunBean;
import gnu.trove.map.hash.TIntObjectHashMap;

public class MSRunBeanAdapter implements Adapter<MSRunBean> {
	private final MsRun msRun;
	private final static TIntObjectHashMap<MSRunBean> map = new TIntObjectHashMap<MSRunBean>();

	public MSRunBeanAdapter(MsRun msRun) {
		this.msRun = msRun;
	}

	@Override
	public MSRunBean adapt() {
		if (map.containsKey(msRun.getId()))
			return map.get(msRun.getId());
		MSRunBean ret = new MSRunBean();
		map.put(msRun.getId(), ret);
		ret.setDate(msRun.getDate());
		ret.setPath(msRun.getPath());
		ret.setRunID(msRun.getRunId());
		ret.setDbID(msRun.getId());
		ret.setProject(new ProjectBeanAdapter(msRun.getProject()).adapt());
		return ret;
	}

}
