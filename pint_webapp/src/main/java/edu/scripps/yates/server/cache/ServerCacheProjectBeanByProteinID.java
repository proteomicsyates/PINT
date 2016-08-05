package edu.scripps.yates.server.cache;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import edu.scripps.yates.shared.cache.Cache;
import edu.scripps.yates.shared.model.ProjectBean;
import edu.scripps.yates.shared.util.SharedConstants;

public class ServerCacheProjectBeanByProteinID implements Cache<ProjectBean, Integer> {
	private static final Map<Integer, ProjectBean> map = new HashMap<Integer, ProjectBean>();
	private static ServerCacheProjectBeanByProteinID instance;

	private ServerCacheProjectBeanByProteinID() {

	}

	@Override
	public Integer processKey(Integer key) {
		// return key.replaceAll(" ", "");
		return key;
	}

	public static ServerCacheProjectBeanByProteinID getInstance() {
		if (instance == null) {
			instance = new ServerCacheProjectBeanByProteinID();
		}
		return instance;
	}

	@Override
	public void addtoCache(ProjectBean t, Integer key) {
		Integer processedKey = processKey(key);
		if (SharedConstants.SERVER_CACHE_ENABLED)
			map.put(processedKey, t);

	}

	@Override
	public ProjectBean getFromCache(Integer key) {
		Integer processedKey = processKey(key);
		return map.get(processedKey);
	}

	@Override
	public void removeFromCache(Integer key) {
		Integer processedKey = processKey(key);
		map.remove(processedKey);

	}

	@Override
	public boolean contains(Integer key) {
		Integer processedKey = processKey(key);
		return map.containsKey(processedKey);
	}

	@Override
	public boolean containsAll(Collection<Integer> keys) {
		for (Integer key : keys) {
			Integer processedKey = processKey(key);
			if (!contains(processedKey))
				return false;
		}
		return true;
	}

	@Override
	public Set<ProjectBean> getFromCache(Collection<Integer> keys) {
		Set<ProjectBean> ret = new HashSet<ProjectBean>();
		for (Integer key : keys) {
			Integer processedKey = processKey(key);
			if (contains(processedKey))
				ret.add(getFromCache(processedKey));
		}
		return ret;
	}

	@Override
	public void clearCache() {
		map.clear();
	}
}
