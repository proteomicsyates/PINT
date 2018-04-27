package edu.scripps.yates.annotations.util;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import edu.scripps.yates.annotations.uniprot.xml.Entry;
import edu.scripps.yates.utilities.cache.AbstractCache;

public class UniprotEntryCache extends AbstractCache<Entry, String> {
	private static boolean enabled = true;

	@Override
	protected Map<String, Entry> createMap() {
		return new HashMap<String, Entry>();
	}

	public static void enable(boolean enable) {
		enabled = enable;
	}

	@Override
	public void addtoCache(Entry t, String key) {
		if (enabled)
			super.addtoCache(t, key);
	}

	@Override
	public void addtoCache(Map<String, Entry> map2) {
		if (enabled)
			super.addtoCache(map2);
	}

	@Override
	public Entry getFromCache(String key) {
		if (enabled)
			return super.getFromCache(key);
		return null;
	}

	@Override
	public Set<Entry> getFromCache(Collection<String> keys) {
		if (enabled)
			return super.getFromCache(keys);
		return null;
	}

	@Override
	public boolean contains(String key) {
		if (enabled)
			return super.contains(key);
		return false;
	}

	@Override
	public Entry removeFromCache(String key) {
		if (enabled)
			return super.removeFromCache(key);
		return null;
	}

	@Override
	public boolean containsAll(Collection<String> keys) {
		if (enabled)
			return super.containsAll(keys);
		return false;
	}

	@Override
	public void clearCache() {
		if (enabled)
			super.clearCache();

	}
}
