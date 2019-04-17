package edu.scripps.yates.server.cache;

import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock.ReadLock;
import java.util.concurrent.locks.ReentrantReadWriteLock.WriteLock;

import edu.scripps.yates.shared.cache.Cache;
import edu.scripps.yates.shared.util.SharedConstants;
import gnu.trove.set.hash.THashSet;

public abstract class AbstractServerCache<T, K> implements Cache<T, K> {
	protected Map<K, T> map;
	protected final ReentrantReadWriteLock lock = new ReentrantReadWriteLock();

	public AbstractServerCache() {
		map = createMap();
	}

	protected abstract Map<K, T> createMap();

	@Override
	public void addtoCache(T t, K key) {
		if (t == null) {
			return;
		}
		final WriteLock writeLock = lock.writeLock();
		try {
			writeLock.lock();
			final K processedKey = processKey(key);
			if (SharedConstants.SERVER_CACHE_ENABLED) {
				map.put(processedKey, t);
			}
		} finally {
			writeLock.unlock();
		}

	}

	@Override
	public T getFromCache(K key) {
		final ReadLock readLock = lock.readLock();
		try {
			readLock.lock();
			final K processedKey = processKey(key);
			return map.get(processedKey);
		} finally {
			readLock.unlock();
		}
	}

	@Override
	public Set<T> getFromCache(Collection<K> keys) {
		final ReadLock readLock = lock.readLock();
		try {
			readLock.lock();
			final Set<T> ret = new THashSet<T>();
			for (final K key : keys) {
				final K processedKey = processKey(key);
				if (contains(processedKey)) {
					ret.add(getFromCache(processedKey));
				}
			}
			return ret;
		} finally {
			readLock.unlock();
		}
	}

	@Override
	public boolean contains(K key) {
		final ReadLock readLock = lock.readLock();
		try {
			readLock.lock();
			final K processedKey = processKey(key);
			return map.containsKey(processedKey);
		} finally {
			readLock.unlock();
		}
	}

	@Override
	public T removeFromCache(K key) {
		final WriteLock writeLock = lock.writeLock();
		try {
			writeLock.lock();
			final K processedKey = processKey(key);
			return map.remove(processedKey);
		} finally {
			writeLock.unlock();
		}
	}

	@Override
	public boolean containsAll(Collection<K> keys) {
		final ReadLock readLock = lock.readLock();
		try {
			readLock.lock();
			for (final K key : keys) {
				final K processedKey = processKey(key);
				if (!contains(processedKey))
					return false;
			}
			return true;
		} finally {
			readLock.unlock();
		}
	}

	@Override
	public K processKey(K key) {
		return key;
	}

	@Override
	public void clearCache() {
		final WriteLock writeLock = lock.writeLock();
		try {
			writeLock.lock();
			map.clear();
		} finally {
			writeLock.unlock();
		}
	}

	@Override
	public boolean isEmpty() {
		return map.isEmpty();
	}

}
