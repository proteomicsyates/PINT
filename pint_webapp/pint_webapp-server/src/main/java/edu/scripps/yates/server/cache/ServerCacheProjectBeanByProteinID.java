package edu.scripps.yates.server.cache;

import edu.scripps.yates.shared.model.ProjectBean;
import gnu.trove.map.hash.TIntObjectHashMap;

public class ServerCacheProjectBeanByProteinID extends AbstractServerCacheByInteger<ProjectBean> {
	private static ServerCacheProjectBeanByProteinID instance;

	public static ServerCacheProjectBeanByProteinID getInstance() {
		if (instance == null) {
			instance = new ServerCacheProjectBeanByProteinID();
		}
		return instance;
	}

	@Override
	protected TIntObjectHashMap<ProjectBean> createMap() {
		return new TIntObjectHashMap<ProjectBean>();
	}
}
