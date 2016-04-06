package edu.scripps.yates.server.cache;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import edu.scripps.yates.shared.cache.Cache;
import edu.scripps.yates.shared.model.PSMBean;
import edu.scripps.yates.shared.util.SharedConstants;

public class ServerCachePSMBeansByProjectTag implements
		Cache<List<PSMBean>, String> {
	private static final HashMap<String, List<PSMBean>> cachedPSMBeans = new HashMap<String, List<PSMBean>>();

	private static ServerCachePSMBeansByProjectTag instance;

	private ServerCachePSMBeansByProjectTag() {

	}

	public static ServerCachePSMBeansByProjectTag getInstance() {
		if (instance == null) {
			instance = new ServerCachePSMBeansByProjectTag();
		}
		return instance;
	}

	@Override
	public void addtoCache(List<PSMBean> PSMs, String key) {
		if (SharedConstants.SERVER_CACHE_ENABLED)
			cachedPSMBeans.put(key, PSMs);
	}

	public void addtoCache(PSMBean psm, String key) {
		if (SharedConstants.SERVER_CACHE_ENABLED) {
			if (cachedPSMBeans.containsKey(key)) {
				cachedPSMBeans.get(key).add(psm);
			} else {
				List<PSMBean> list = new ArrayList<PSMBean>();
				list.add(psm);
				cachedPSMBeans.put(key, list);
			}
		}
	}

	@Override
	public List<PSMBean> getFromCache(String key) {
		return cachedPSMBeans.get(key);
	}

	@Override
	public void removeFromCache(String key) {
		cachedPSMBeans.remove(key);
	}

	@Override
	public boolean contains(String key) {
		return cachedPSMBeans.containsKey(key);
	}

	@Override
	public boolean containsAll(Collection<String> keys) {
		for (String key : keys) {
			if (!contains(key))
				return false;
		}
		return true;
	}

	@Override
	public Set<List<PSMBean>> getFromCache(Collection<String> keys) {
		Set<List<PSMBean>> ret = new HashSet<List<PSMBean>>();
		for (String key : keys) {
			if (contains(key))
				ret.add(getFromCache(key));
		}
		return ret;
	}

	@Override
	public String processKey(String key) {

		return key;
	}
}
