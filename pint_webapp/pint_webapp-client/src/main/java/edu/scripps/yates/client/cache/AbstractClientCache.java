package edu.scripps.yates.client.cache;

import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import edu.scripps.yates.shared.cache.Cache;
import edu.scripps.yates.shared.util.SharedConstants;

public abstract class AbstractClientCache<T, K> implements Cache<T, K> {
	protected Map<K, T> map;

	public AbstractClientCache() {
		map = createMap();
	}

	protected abstract Map<K, T> createMap();

	@Override
	public void addtoCache(T t, K key) {
		if (t == null) {
			return;
		}

		K processedKey = processKey(key);
		if (SharedConstants.CLIENT_CACHE_ENABLED) {
			map.put(processedKey, t);
		}

	}

	@Override
	public T getFromCache(K key) {

		K processedKey = processKey(key);
		return map.get(processedKey);

	}

	@Override
	public Set<T> getFromCache(Collection<K> keys) {

		Set<T> ret = new HashSet<T>();
		for (K key : keys) {
			K processedKey = processKey(key);
			if (contains(processedKey)) {
				ret.add(getFromCache(processedKey));
			}
		}
		return ret;

	}

	@Override
	public boolean contains(K key) {

		K processedKey = processKey(key);
		return map.containsKey(processedKey);

	}

	@Override
	public T removeFromCache(K key) {

		K processedKey = processKey(key);
		return map.remove(processedKey);

	}

	@Override
	public boolean containsAll(Collection<K> keys) {

		for (K key : keys) {
			K processedKey = processKey(key);
			if (!contains(processedKey))
				return false;
		}
		return true;

	}

	@Override
	public K processKey(K key) {
		return key;
	}

	@Override
	public void clearCache() {

		map.clear();

	}

	@Override
	public boolean isEmpty() {
		return map.isEmpty();
	}

}
