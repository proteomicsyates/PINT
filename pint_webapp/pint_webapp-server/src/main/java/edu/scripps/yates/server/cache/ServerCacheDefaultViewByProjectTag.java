package edu.scripps.yates.server.cache;

import java.util.Map;

import edu.scripps.yates.shared.util.DefaultView;
import gnu.trove.map.hash.THashMap;

public class ServerCacheDefaultViewByProjectTag extends AbstractServerCache<DefaultView, String> {
	private static ServerCacheDefaultViewByProjectTag instance;

	public static ServerCacheDefaultViewByProjectTag getInstance() {
		if (instance == null) {
			instance = new ServerCacheDefaultViewByProjectTag();
		}
		return instance;
	}

	@Override
	protected Map<String, DefaultView> createMap() {

		return new THashMap<String, DefaultView>();
	}
}
