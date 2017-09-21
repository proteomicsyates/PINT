package edu.scripps.yates.server.cache;

import java.util.Collection;
import java.util.concurrent.locks.ReentrantReadWriteLock.WriteLock;

public abstract class AbstractServerCacheForCollections<T, C extends Collection<T>, K>
		extends AbstractServerCache<C, K> {

	@Override
	public void addtoCache(C collection, K key) {
		WriteLock writeLock = lock.writeLock();
		try {
			writeLock.lock();
			if (map.containsKey(key)) {
				map.get(key).addAll(collection);
			} else {
				C col = createCollection();
				col.addAll(collection);
				map.put(key, col);
			}
		} finally {
			writeLock.unlock();
		}

	}

	protected abstract C createCollection();
}
