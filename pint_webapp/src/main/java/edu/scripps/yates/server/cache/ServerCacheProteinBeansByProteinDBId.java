package edu.scripps.yates.server.cache;

import java.util.Collection;
import java.util.Set;

import edu.scripps.yates.shared.cache.Cache;
import edu.scripps.yates.shared.model.ProteinBean;
import gnu.trove.map.hash.TIntObjectHashMap;
import gnu.trove.set.hash.THashSet;

public class ServerCacheProteinBeansByProteinDBId implements Cache<ProteinBean, Integer> {
	private static final TIntObjectHashMap<ProteinBean> cachedProteinBeans = new TIntObjectHashMap<ProteinBean>();

	private static ServerCacheProteinBeansByProteinDBId instance;

	private ServerCacheProteinBeansByProteinDBId() {

	}

	public boolean isEmpty() {
		return cachedProteinBeans.isEmpty();
	}

	public static ServerCacheProteinBeansByProteinDBId getInstance() {
		if (instance == null) {
			instance = new ServerCacheProteinBeansByProteinDBId();
		}
		return instance;
	}

	@Override
	public void addtoCache(ProteinBean protein, Integer key) {
		// if (SharedConstants.SERVER_CACHE_ENABLED)
		cachedProteinBeans.put(key, protein);
	}

	@Override
	public ProteinBean getFromCache(Integer key) {
		return cachedProteinBeans.get(key);
	}

	@Override
	public void removeFromCache(Integer key) {
		cachedProteinBeans.remove(key);
	}

	@Override
	public boolean contains(Integer key) {
		return cachedProteinBeans.containsKey(key);
	}

	@Override
	public boolean containsAll(Collection<Integer> keys) {
		for (Integer key : keys) {
			if (!contains(key))
				return false;
		}
		return true;
	}

	@Override
	public Set<ProteinBean> getFromCache(Collection<Integer> keys) {
		Set<ProteinBean> ret = new THashSet<ProteinBean>();
		for (Integer key : keys) {
			if (contains(key))
				ret.add(getFromCache(key));
		}
		return ret;
	}

	@Override
	public Integer processKey(Integer key) {

		return key;
	}

	@Override
	public void clearCache() {
		cachedProteinBeans.clear();
	}
}
