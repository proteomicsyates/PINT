package edu.scripps.yates.server.cache;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import edu.scripps.yates.shared.cache.Cache;
import edu.scripps.yates.shared.model.PSMBean;
import edu.scripps.yates.shared.util.SharedConstants;

public class ServerCachePSMBeansByPSMDBId implements Cache<PSMBean, Integer> {
	private static final HashMap<Integer, PSMBean> cachedPSMBeans = new HashMap<Integer, PSMBean>();

	private static ServerCachePSMBeansByPSMDBId instance;

	private ServerCachePSMBeansByPSMDBId() {

	}

	public static ServerCachePSMBeansByPSMDBId getInstance() {
		if (instance == null) {
			instance = new ServerCachePSMBeansByPSMDBId();
		}
		return instance;
	}

	@Override
	public void addtoCache(PSMBean psm, Integer key) {
		if (SharedConstants.SERVER_CACHE_ENABLED)
			cachedPSMBeans.put(key, psm);
	}

	@Override
	public PSMBean getFromCache(Integer key) {
		return cachedPSMBeans.get(key);
	}

	@Override
	public void removeFromCache(Integer key) {
		cachedPSMBeans.remove(key);
	}

	@Override
	public boolean contains(Integer key) {
		return cachedPSMBeans.containsKey(key);
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
	public Set<PSMBean> getFromCache(Collection<Integer> keys) {
		Set<PSMBean> ret = new HashSet<PSMBean>();
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
}
