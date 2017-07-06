package edu.scripps.yates.server.cache;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;

import edu.scripps.yates.shared.cache.Cache;
import edu.scripps.yates.shared.model.ProteinBean;
import edu.scripps.yates.shared.util.SharedConstants;
import gnu.trove.map.hash.THashMap;
import gnu.trove.set.hash.THashSet;

public class ServerCacheProteinBeansByProjectTag implements Cache<List<ProteinBean>, String> {
	private static final Map<String, List<ProteinBean>> map = new THashMap<String, List<ProteinBean>>();
	private static final Logger log = Logger.getLogger(ServerCacheDefaultViewByProjectTag.class);
	private static ServerCacheProteinBeansByProjectTag instance;
	private static final boolean ENABLED = true;

	private ServerCacheProteinBeansByProjectTag() {

	}

	public static ServerCacheProteinBeansByProjectTag getInstance() {
		if (instance == null) {
			instance = new ServerCacheProteinBeansByProjectTag();
		}
		return instance;
	}

	@Override
	public void addtoCache(List<ProteinBean> proteins, String key) {
		if (SharedConstants.SERVER_CACHE_ENABLED && ENABLED && proteins != null && !proteins.isEmpty()) {
			log.info("Adding to cache " + proteins.size() + " proteins with key: " + key);
			if (map.containsKey(key)) {
				map.get(key).addAll(proteins);
			} else {
				map.put(key, proteins);
			}
		}
	}

	@Override
	public List<ProteinBean> getFromCache(String key) {
		return map.get(key);
	}

	@Override
	public void removeFromCache(String key) {
		map.remove(key);
	}

	@Override
	public boolean contains(String key) {
		return map.containsKey(key);
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
	public Set<List<ProteinBean>> getFromCache(Collection<String> keys) {
		Set<List<ProteinBean>> ret = new THashSet<List<ProteinBean>>();
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

	@Override
	public void clearCache() {
		map.clear();
	}
}
