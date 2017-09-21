package edu.scripps.yates.client.cache;

import java.util.HashMap;
import java.util.Map;

import edu.scripps.yates.shared.util.DefaultView;

public class ClientCacheDefaultViewByProjectTag extends AbstractClientCache<DefaultView, String> {
	private static ClientCacheDefaultViewByProjectTag instance;

	public static ClientCacheDefaultViewByProjectTag getInstance() {
		if (instance == null) {
			instance = new ClientCacheDefaultViewByProjectTag();
		}
		return instance;
	}

	@Override
	protected Map<String, DefaultView> createMap() {

		return new HashMap<String, DefaultView>();
	}

}
