package edu.scripps.yates.client.cache;

import java.util.HashMap;
import java.util.Map;

import edu.scripps.yates.shared.model.ProjectBean;

public class ClientCacheProjectBeansByProjectTag extends AbstractClientCache<ProjectBean, String> {
	private static ClientCacheProjectBeansByProjectTag instance;

	public static ClientCacheProjectBeansByProjectTag getInstance() {
		if (instance == null) {
			instance = new ClientCacheProjectBeansByProjectTag();
		}
		return instance;
	}

	@Override
	protected Map<String, ProjectBean> createMap() {
		return new HashMap<String, ProjectBean>();
	}
}
