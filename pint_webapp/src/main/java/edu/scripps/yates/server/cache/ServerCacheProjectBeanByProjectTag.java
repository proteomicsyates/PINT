package edu.scripps.yates.server.cache;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import edu.scripps.yates.shared.cache.Cache;
import edu.scripps.yates.shared.model.ProjectBean;
import edu.scripps.yates.shared.util.SharedConstants;

public class ServerCacheProjectBeanByProjectTag implements
		Cache<ProjectBean, String> {
	private static final Map<String, ProjectBean> map = new HashMap<String, ProjectBean>();
	private static ServerCacheProjectBeanByProjectTag instance;

	private ServerCacheProjectBeanByProjectTag() {

	}

	@Override
	public String processKey(String key) {
		// return key.replaceAll(" ", "");
		return key;
	}

	public static ServerCacheProjectBeanByProjectTag getInstance() {
		if (instance == null) {
			instance = new ServerCacheProjectBeanByProjectTag();
		}
		return instance;
	}

	@Override
	public void addtoCache(ProjectBean t, String key) {
		String processedKey = processKey(key);
		if (SharedConstants.SERVER_CACHE_ENABLED)

			map.put(processedKey, t);

	}

	@Override
	public ProjectBean getFromCache(String key) {
		String processedKey = processKey(key);
		return map.get(processedKey);
	}

	@Override
	public void removeFromCache(String key) {
		String processedKey = processKey(key);
		map.remove(processedKey);

	}

	@Override
	public boolean contains(String key) {
		String processedKey = processKey(key);
		return map.containsKey(processedKey);
	}

	@Override
	public boolean containsAll(Collection<String> keys) {
		for (String key : keys) {
			String processedKey = processKey(key);
			if (!contains(processedKey))
				return false;
		}
		return true;
	}

	@Override
	public Set<ProjectBean> getFromCache(Collection<String> keys) {
		Set<ProjectBean> ret = new HashSet<ProjectBean>();
		for (String key : keys) {
			String processedKey = processKey(key);
			if (contains(processedKey))
				ret.add(getFromCache(processedKey));
		}
		return ret;
	}

}
