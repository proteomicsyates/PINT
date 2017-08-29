package edu.scripps.yates.server.cache;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

import edu.scripps.yates.shared.cache.Cache;
import edu.scripps.yates.shared.util.SharedConstants;
import gnu.trove.map.hash.THashMap;
import gnu.trove.set.hash.THashSet;

public class ServerCacheHiddenPTMsByProjectTag implements Cache<Set<String>, String> {
	private static final Map<String, Set<String>> map = new THashMap<String, Set<String>>();
	private static ServerCacheHiddenPTMsByProjectTag instance;

	private ServerCacheHiddenPTMsByProjectTag() {

	}

	@Override
	public String processKey(String key) {
		// return key.replaceAll(" ", "");
		return key;
	}

	public static ServerCacheHiddenPTMsByProjectTag getInstance() {
		if (instance == null) {
			instance = new ServerCacheHiddenPTMsByProjectTag();
		}
		return instance;
	}

	@Override
	public void addtoCache(Set<String> t, String key) {
		String processedKey = processKey(key);
		if (SharedConstants.SERVER_CACHE_ENABLED)

			map.put(processedKey, t);

	}

	@Override
	public Set<String> getFromCache(String key) {
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
	public Set<Set<String>> getFromCache(Collection<String> keys) {
		Set<Set<String>> ret = new THashSet<Set<String>>();
		for (String key : keys) {
			String processedKey = processKey(key);
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
