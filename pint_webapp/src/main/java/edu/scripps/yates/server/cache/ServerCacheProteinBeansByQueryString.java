package edu.scripps.yates.server.cache;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

import edu.scripps.yates.shared.cache.Cache;
import edu.scripps.yates.shared.model.ProteinBean;
import edu.scripps.yates.shared.util.SharedConstants;
import gnu.trove.map.hash.THashMap;
import gnu.trove.set.hash.THashSet;

public class ServerCacheProteinBeansByQueryString implements Cache<List<ProteinBean>, String> {
	private static final Map<String, List<ProteinBean>> map = new THashMap<String, List<ProteinBean>>();
	private static ServerCacheProteinBeansByQueryString instance;

	private ServerCacheProteinBeansByQueryString() {

	}

	@Override
	public String processKey(String key) {
		return key.replaceAll(" ", "");
	}

	public static ServerCacheProteinBeansByQueryString getInstance() {
		if (instance == null) {
			instance = new ServerCacheProteinBeansByQueryString();
		}
		return instance;
	}

	@Override
	public void addtoCache(List<ProteinBean> t, String key) {

		String processedKey = processKey(key);
		if (SharedConstants.SERVER_CACHE_ENABLED && t != null && !t.isEmpty()) {
			if (map.containsKey(processedKey)) {
				map.get(processedKey).addAll(t);
			} else {
				List<ProteinBean> set = new ArrayList<ProteinBean>();
				set.addAll(t);
				map.put(processedKey, set);
			}
		}
	}

	@Override
	public List<ProteinBean> getFromCache(String key) {
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
	public Set<List<ProteinBean>> getFromCache(Collection<String> keys) {
		Set<List<ProteinBean>> ret = new THashSet<List<ProteinBean>>();
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
