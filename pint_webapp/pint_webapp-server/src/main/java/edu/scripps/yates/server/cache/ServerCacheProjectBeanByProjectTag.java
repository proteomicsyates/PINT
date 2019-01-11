package edu.scripps.yates.server.cache;

import java.util.Map;

import edu.scripps.yates.shared.model.ProjectBean;
import gnu.trove.map.hash.THashMap;

public class ServerCacheProjectBeanByProjectTag extends AbstractServerCache<ProjectBean, String> {
	private static ServerCacheProjectBeanByProjectTag instance;

	public static ServerCacheProjectBeanByProjectTag getInstance() {
		if (instance == null) {
			instance = new ServerCacheProjectBeanByProjectTag();
		}
		return instance;
	}

	@Override
	protected Map<String, ProjectBean> createMap() {

		return new THashMap<String, ProjectBean>();
	}
}
