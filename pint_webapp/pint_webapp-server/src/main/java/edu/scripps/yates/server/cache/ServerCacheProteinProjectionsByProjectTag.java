package edu.scripps.yates.server.cache;

import java.util.List;
import java.util.Map;

import edu.scripps.yates.shared.model.ProteinProjection;
import gnu.trove.map.hash.THashMap;

public class ServerCacheProteinProjectionsByProjectTag extends AbstractServerCache<List<ProteinProjection>, String> {
	private static ServerCacheProteinProjectionsByProjectTag instance;

	public static ServerCacheProteinProjectionsByProjectTag getInstance() {
		if (instance == null) {
			instance = new ServerCacheProteinProjectionsByProjectTag();
		}
		return instance;
	}

	@Override
	protected Map<String, List<ProteinProjection>> createMap() {
		return new THashMap<String, List<ProteinProjection>>();
	}
}
