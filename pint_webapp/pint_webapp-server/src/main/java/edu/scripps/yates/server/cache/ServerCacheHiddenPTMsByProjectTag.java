package edu.scripps.yates.server.cache;

import java.util.Map;
import java.util.Set;

import gnu.trove.map.hash.THashMap;
import gnu.trove.set.hash.THashSet;

public class ServerCacheHiddenPTMsByProjectTag extends AbstractServerCacheForCollections<String, Set<String>, String> {
	private static ServerCacheHiddenPTMsByProjectTag instance;

	public static ServerCacheHiddenPTMsByProjectTag getInstance() {
		if (instance == null) {
			instance = new ServerCacheHiddenPTMsByProjectTag();
		}
		return instance;
	}

	@Override
	protected Map<String, Set<String>> createMap() {
		return new THashMap<String, Set<String>>();
	}

	@Override
	protected Set<String> createCollection() {
		return new THashSet<String>();
	}

}
