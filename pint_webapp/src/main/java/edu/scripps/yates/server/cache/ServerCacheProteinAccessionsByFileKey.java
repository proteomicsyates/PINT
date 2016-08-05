package edu.scripps.yates.server.cache;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import edu.scripps.yates.shared.cache.Cache;
import edu.scripps.yates.shared.util.SharedConstants;

public class ServerCacheProteinAccessionsByFileKey implements Cache<List<String>, String> {
	private static final HashMap<String, List<String>> map = new HashMap<String, List<String>>();

	private static ServerCacheProteinAccessionsByFileKey instance;

	private ServerCacheProteinAccessionsByFileKey() {

	}

	public static ServerCacheProteinAccessionsByFileKey getInstance() {
		if (instance == null) {
			instance = new ServerCacheProteinAccessionsByFileKey();
		}
		return instance;
	}

	@Override
	public void addtoCache(List<String> proteins, String key) {
		if (SharedConstants.SERVER_CACHE_ENABLED)
			map.put(key, proteins);
	}

	@Override
	public List<String> getFromCache(String key) {
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
	public Set<List<String>> getFromCache(Collection<String> keys) {
		Set<List<String>> ret = new HashSet<List<String>>();
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
