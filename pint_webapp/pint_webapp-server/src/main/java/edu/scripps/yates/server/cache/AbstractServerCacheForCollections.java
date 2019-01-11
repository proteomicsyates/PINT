package edu.scripps.yates.server.cache;

import java.util.Collection;
import java.util.concurrent.locks.ReentrantReadWriteLock.WriteLock;

public abstract class AbstractServerCacheForCollections<T, C extends Collection<T>, K>
		extends AbstractServerCache<C, K> {

	@Override
	public void addtoCache(C collection, K key) {
		final WriteLock writeLock = lock.writeLock();
		try {
			writeLock.lock();
			final K processedKey = processKey(key);
			if (map.containsKey(processedKey)) {
				map.get(processedKey).addAll(collection);
			} else {
				final C col = createCollection();
				col.addAll(collection);
				map.put(processedKey, col);
			}
		} finally {
			writeLock.unlock();
		}

	}

	protected abstract C createCollection();
}
