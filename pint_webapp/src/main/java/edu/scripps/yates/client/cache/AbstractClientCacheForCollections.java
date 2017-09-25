package edu.scripps.yates.client.cache;

import java.util.Collection;

public abstract class AbstractClientCacheForCollections<T, C extends Collection<T>, K>
		extends AbstractClientCache<C, K> {

	@Override
	public void addtoCache(C collection, K key) {

		if (map.containsKey(key)) {
			map.get(key).addAll(collection);
		} else {
			C col = createCollection();
			col.addAll(collection);
			map.put(key, col);
		}

	}

	protected abstract C createCollection();
}
