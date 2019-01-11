package edu.scripps.yates.client.cache;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class ClientCacheConditionsByProject extends AbstractClientCacheForCollections<String, Set<String>, String> {
	private static ClientCacheConditionsByProject instance;

	public static ClientCacheConditionsByProject getInstance() {
		if (instance == null) {
			instance = new ClientCacheConditionsByProject();
		}
		return instance;
	}

	@Override
	protected Set<String> createCollection() {

		return new HashSet<String>();
	}

	@Override
	protected Map<String, Set<String>> createMap() {

		return new HashMap<String, Set<String>>();
	}
}
