package edu.scripps.yates.server.cache;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

import edu.scripps.yates.shared.cache.Cache;
import edu.scripps.yates.shared.util.FileDescriptor;
import edu.scripps.yates.shared.util.SharedConstants;
import gnu.trove.map.hash.THashMap;
import gnu.trove.set.hash.THashSet;

public class ServerCacheProteinFileDescriptorByClientIDAndQuery implements Cache<FileDescriptor, String> {
	private static final Map<String, FileDescriptor> map = new THashMap<String, FileDescriptor>();
	private static ServerCacheProteinFileDescriptorByClientIDAndQuery instance;
	private final static boolean enabled = false;

	private ServerCacheProteinFileDescriptorByClientIDAndQuery() {

	}

	@Override
	public String processKey(String key) {
		// return key.replaceAll(" ", "");
		return key;
	}

	public static ServerCacheProteinFileDescriptorByClientIDAndQuery getInstance() {
		if (instance == null) {
			instance = new ServerCacheProteinFileDescriptorByClientIDAndQuery();
		}
		return instance;
	}

	@Override
	public void addtoCache(FileDescriptor t, String key) {
		String processedKey = processKey(key);
		if (SharedConstants.SERVER_CACHE_ENABLED && t != null)

			map.put(processedKey, t);

	}

	@Override
	public FileDescriptor getFromCache(String key) {
		String processedKey = processKey(key);
		return map.get(processedKey);
	}

	@Override
	public void removeFromCache(String key) {
		String processedKey = processKey(key);
		map.remove(processedKey);

	}

	@Override
	public boolean contains(String key) {
		String processedKey = processKey(key);
		return map.containsKey(processedKey);
	}

	@Override
	public boolean containsAll(Collection<String> keys) {
		for (String key : keys) {
			String processedKey = processKey(key);
			if (!contains(processedKey))
				return false;
		}
		return true;
	}

	@Override
	public Set<FileDescriptor> getFromCache(Collection<String> keys) {
		Set<FileDescriptor> ret = new THashSet<FileDescriptor>();
		for (String key : keys) {
			String processedKey = processKey(key);
			if (contains(processedKey))
				ret.add(getFromCache(processedKey));
		}
		return ret;
	}

	@Override
	public void clearCache() {
		map.clear();
	}
}
