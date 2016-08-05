package edu.scripps.yates.server.cache;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import edu.scripps.yates.shared.cache.Cache;
import edu.scripps.yates.shared.model.OrganismBean;
import edu.scripps.yates.shared.util.SharedConstants;

public class ServerCacheOrganismBeansByProjectName implements Cache<Set<OrganismBean>, String> {
	private static final Map<String, Set<OrganismBean>> map = new HashMap<String, Set<OrganismBean>>();
	private static ServerCacheOrganismBeansByProjectName instance;

	private ServerCacheOrganismBeansByProjectName() {

	}

	public static ServerCacheOrganismBeansByProjectName getInstance() {
		if (instance == null) {
			instance = new ServerCacheOrganismBeansByProjectName();
		}
		return instance;
	}

	@Override
	public void addtoCache(Set<OrganismBean> t, String key) {
		if (SharedConstants.SERVER_CACHE_ENABLED)
			map.put(key, t);

	}

	@Override
	public Set<OrganismBean> getFromCache(String key) {
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
	public Set<Set<OrganismBean>> getFromCache(Collection<String> keys) {
		Set<Set<OrganismBean>> ret = new HashSet<Set<OrganismBean>>();
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
