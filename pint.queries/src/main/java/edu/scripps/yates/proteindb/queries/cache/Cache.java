package edu.scripps.yates.proteindb.queries.cache;

import java.util.Collection;
import java.util.Set;

public interface Cache<T, V> {
	public void addtoCache(T t, V key);

	public T getFromCache(V key);

	public Set<T> getFromCache(Collection<V> keys);

	public boolean contains(V key);

	public void removeFromCache(V key);

	boolean containsAll(Collection<V> keys);

}
