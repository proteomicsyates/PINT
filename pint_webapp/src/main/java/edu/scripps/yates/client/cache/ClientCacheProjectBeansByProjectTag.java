package edu.scripps.yates.client.cache;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import edu.scripps.yates.shared.cache.Cache;
import edu.scripps.yates.shared.model.ProjectBean;
import edu.scripps.yates.shared.util.SharedConstants;

public class ClientCacheProjectBeansByProjectTag implements Cache<ProjectBean, String> {
	private static final Map<String, ProjectBean> map = new HashMap<String, ProjectBean>();
	private static ClientCacheProjectBeansByProjectTag instance;

	private ClientCacheProjectBeansByProjectTag() {

	}

	public static ClientCacheProjectBeansByProjectTag getInstance() {
		if (instance == null) {
			instance = new ClientCacheProjectBeansByProjectTag();
		}
		return instance;
	}

	@Override
	public void addtoCache(ProjectBean t, String key) {
		if (SharedConstants.CLIENT_CACHE_ENABLED)
			map.put(key, t);

	}

	@Override
	public ProjectBean getFromCache(String key) {
		return map.get(key);
	}

	@Override
	public void removeFromCache(String key) {
		map.remove(key);

	}

	@Override
	public boolean contains(String key) {

		return map.containsKey(key);
	}

	@Override
	public boolean containsAll(Collection<String> keys) {
		for (String key : keys) {
			if (!contains(key))
				return false;
		}
		return true;
	}

	@Override
	public Set<ProjectBean> getFromCache(Collection<String> keys) {
		Set<ProjectBean> ret = new HashSet<ProjectBean>();
		for (String key : keys) {
			if (contains(key))
				ret.add(getFromCache(key));
		}
		return ret;
	}

	@Override
	public String processKey(String key) {
		return key;
	}

	@Override
	public void clearCache() {
		map.clear();
	}
}
