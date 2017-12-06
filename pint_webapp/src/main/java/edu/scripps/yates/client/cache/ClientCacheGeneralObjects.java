package edu.scripps.yates.client.cache;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ClientCacheGeneralObjects extends AbstractClientCacheForCollections<String, List<String>, GeneralObject> {
	private static ClientCacheGeneralObjects instance;

	public static ClientCacheGeneralObjects getInstance() {
		if (instance == null) {
			instance = new ClientCacheGeneralObjects();
		}
		return instance;
	}

	@Override
	protected List<String> createCollection() {
		return new ArrayList<String>();
	}

	@Override
	protected Map<GeneralObject, List<String>> createMap() {
		return new HashMap<GeneralObject, List<String>>();
	}

}
