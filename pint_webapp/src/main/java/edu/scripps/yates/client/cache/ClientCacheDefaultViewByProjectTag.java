package edu.scripps.yates.client.cache;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import edu.scripps.yates.shared.cache.Cache;
import edu.scripps.yates.shared.util.DefaultView;
import edu.scripps.yates.shared.util.SharedConstants;

public class ClientCacheDefaultViewByProjectTag implements
		Cache<DefaultView, String> {
	private static final Map<String, DefaultView> map = new HashMap<String, DefaultView>();
	private static ClientCacheDefaultViewByProjectTag instance;

	private ClientCacheDefaultViewByProjectTag() {

	}

	@Override
	public String processKey(String key) {
		// return key.replaceAll(" ", "");
		return key;
	}

	public static ClientCacheDefaultViewByProjectTag getInstance() {
		if (instance == null) {
			instance = new ClientCacheDefaultViewByProjectTag();
		}
		return instance;
	}

	@Override
	public void addtoCache(DefaultView t, String key) {
		if (SharedConstants.CLIENT_CACHE_ENABLED) {
			String processedKey = processKey(key);

			map.put(processedKey, t);
		}
	}

	@Override
	public DefaultView getFromCache(String key) {
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
	public Set<DefaultView> getFromCache(Collection<String> keys) {
		Set<DefaultView> ret = new HashSet<DefaultView>();
		for (String key : keys) {
			String processedKey = processKey(key);
			if (contains(processedKey))
				ret.add(getFromCache(processedKey));
		}
		return ret;
	}

}
