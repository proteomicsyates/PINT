package edu.scripps.yates.client.cache;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import edu.scripps.yates.shared.cache.Cache;
import edu.scripps.yates.shared.util.SharedConstants;

public class ClientCacheConditionsByProject implements
		Cache<Set<String>, String> {
	private static final Map<String, Set<String>> map = new HashMap<String, Set<String>>();
	private static ClientCacheConditionsByProject instance;

	private ClientCacheConditionsByProject() {

	}

	public static ClientCacheConditionsByProject getInstance() {
		if (instance == null) {
			instance = new ClientCacheConditionsByProject();
		}
		return instance;
	}

	@Override
	public void addtoCache(Set<String> t, String key) {
		if (SharedConstants.CLIENT_CACHE_ENABLED) {
			if (map.containsKey(key)) {
				map.get(key).addAll(t);
			} else {
				Set<String> set = new HashSet<String>();
				set.addAll(t);
				map.put(key, set);
			}
		}
	}

	@Override
	public Set<String> getFromCache(String key) {
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
	public Set<Set<String>> getFromCache(Collection<String> keys) {
		Set<Set<String>> ret = new HashSet<Set<String>>();
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
}
