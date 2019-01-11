package edu.scripps.yates.server.cache;

import java.util.Collection;
import java.util.Set;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock.ReadLock;
import java.util.concurrent.locks.ReentrantReadWriteLock.WriteLock;

import edu.scripps.yates.shared.cache.Cache;
import edu.scripps.yates.shared.util.SharedConstants;
import gnu.trove.map.hash.TIntObjectHashMap;
import gnu.trove.set.hash.THashSet;

public abstract class AbstractServerCacheByInteger<T> implements Cache<T, Integer> {
	protected TIntObjectHashMap<T> map;
	protected final ReentrantReadWriteLock lock = new ReentrantReadWriteLock();

	public AbstractServerCacheByInteger() {
		map = createMap();
	}

	protected abstract TIntObjectHashMap<T> createMap();

	@Override
	public void addtoCache(T t, Integer key) {
		if (t == null) {
			return;
		}
		WriteLock writeLock = lock.writeLock();
		try {
			writeLock.lock();
			Integer processedKey = processKey(key);
			if (SharedConstants.SERVER_CACHE_ENABLED) {
				map.put(processedKey, t);
			}
		} finally {
			writeLock.unlock();
		}

	}

	@Override
	public T getFromCache(Integer key) {
		ReadLock readLock = lock.readLock();
		try {
			readLock.lock();
			Integer processedKey = processKey(key);
			return map.get(processedKey);
		} finally {
			readLock.unlock();
		}
	}

	@Override
	public Set<T> getFromCache(Collection<Integer> keys) {
		ReadLock readLock = lock.readLock();
		try {
			readLock.lock();
			Set<T> ret = new THashSet<T>();
			for (Integer key : keys) {
				Integer processedKey = processKey(key);
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
	public boolean contains(Integer key) {
		ReadLock readLock = lock.readLock();
		try {
			readLock.lock();
			Integer processedKey = processKey(key);
			return map.containsKey(processedKey);
		} finally {
			readLock.unlock();
		}
	}

	@Override
	public T removeFromCache(Integer key) {
		WriteLock writeLock = lock.writeLock();
		try {
			writeLock.lock();
			Integer processedKey = processKey(key);
			return map.remove(processedKey);
		} finally {
			writeLock.unlock();
		}
	}

	@Override
	public boolean containsAll(Collection<Integer> keys) {
		ReadLock readLock = lock.readLock();
		try {
			readLock.lock();
			for (Integer key : keys) {
				Integer processedKey = processKey(key);
				if (!contains(processedKey))
					return false;
			}
			return true;
		} finally {
			readLock.unlock();
		}
	}

	@Override
	public Integer processKey(Integer key) {
		return key;
	}

	@Override
	public void clearCache() {
		WriteLock writeLock = lock.writeLock();
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
