package edu.scripps.yates.client.cache;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import edu.scripps.yates.shared.cache.Cache;
import edu.scripps.yates.shared.util.FileDescriptor;
import edu.scripps.yates.shared.util.SharedConstants;

public class ClientCacheProteinFileDescriptorByProjectName implements Cache<FileDescriptor, String> {
	private static final Map<String, FileDescriptor> map = new HashMap<String, FileDescriptor>();
	private static ClientCacheProteinFileDescriptorByProjectName instance;

	private ClientCacheProteinFileDescriptorByProjectName() {

	}

	@Override
	public String processKey(String key) {
		// return key.replaceAll(" ", "");
		return key;
	}

	public static ClientCacheProteinFileDescriptorByProjectName getInstance() {
		if (instance == null) {
			instance = new ClientCacheProteinFileDescriptorByProjectName();
		}
		return instance;
	}

	@Override
	public void addtoCache(FileDescriptor t, String key) {
		if (SharedConstants.CLIENT_CACHE_ENABLED && t != null) {
			String processedKey = processKey(key);

			map.put(processedKey, t);
		}
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
		if (true)
			return false;
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
		Set<FileDescriptor> ret = new HashSet<FileDescriptor>();
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
