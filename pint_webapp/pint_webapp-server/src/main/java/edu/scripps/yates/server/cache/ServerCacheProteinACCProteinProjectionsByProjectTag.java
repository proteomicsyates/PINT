package edu.scripps.yates.server.cache;

import java.util.Map;
import java.util.Set;

import edu.scripps.yates.shared.model.ProteinProjection;
import gnu.trove.map.hash.THashMap;

public class ServerCacheProteinACCProteinProjectionsByProjectTag
		extends AbstractServerCache<Map<String, Set<ProteinProjection>>, String> {
	private static ServerCacheProteinACCProteinProjectionsByProjectTag instance;

	public static ServerCacheProteinACCProteinProjectionsByProjectTag getInstance() {
		if (instance == null) {
			instance = new ServerCacheProteinACCProteinProjectionsByProjectTag();
		}
		return instance;
	}

	@Override
	protected Map<String, Map<String, Set<ProteinProjection>>> createMap() {
		return new THashMap<String, Map<String, Set<ProteinProjection>>>();
	}
}
