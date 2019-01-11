package edu.scripps.yates.server.cache;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import gnu.trove.map.hash.THashMap;

public class ServerCacheProteinAccessionsByFileKey
		extends AbstractServerCacheForCollections<String, List<String>, String> {

	private static ServerCacheProteinAccessionsByFileKey instance;

	public static ServerCacheProteinAccessionsByFileKey getInstance() {
		if (instance == null) {
			instance = new ServerCacheProteinAccessionsByFileKey();
		}
		return instance;
	}

	@Override
	protected Map<String, List<String>> createMap() {
		return new THashMap<String, List<String>>();
	}

	@Override
	protected List<String> createCollection() {
		return new ArrayList<String>();
	}
}
