package edu.scripps.yates.shared.cache;

import java.util.Collection;
import java.util.Set;

public interface Cache<T, V> {
	public void addtoCache(T t, V key);

	public T getFromCache(V key);

	public Set<T> getFromCache(Collection<V> keys);

	public boolean contains(V key);

	public T removeFromCache(V key);

	boolean containsAll(Collection<V> keys);

	public V processKey(V key);

	public void clearCache();

	public boolean isEmpty();
}
