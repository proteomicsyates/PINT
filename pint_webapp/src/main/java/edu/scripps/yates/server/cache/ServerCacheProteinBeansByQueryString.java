package edu.scripps.yates.server.cache;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import edu.scripps.yates.shared.model.ProteinBean;
import gnu.trove.map.hash.THashMap;

public class ServerCacheProteinBeansByQueryString
		extends AbstractServerCacheForCollections<ProteinBean, List<ProteinBean>, String> {
	private static ServerCacheProteinBeansByQueryString instance;

	@Override
	public String processKey(String key) {
		return key;// key.replaceAll(" ", "");
	}

	public static ServerCacheProteinBeansByQueryString getInstance() {
		if (instance == null) {
			instance = new ServerCacheProteinBeansByQueryString();
		}
		return instance;
	}

	@Override
	protected Map<String, List<ProteinBean>> createMap() {
		return new THashMap<String, List<ProteinBean>>();
	}

	@Override
	protected List<ProteinBean> createCollection() {
		return new ArrayList<ProteinBean>();
	}
}
