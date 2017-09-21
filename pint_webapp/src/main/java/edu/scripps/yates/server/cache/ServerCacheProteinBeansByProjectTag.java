package edu.scripps.yates.server.cache;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import edu.scripps.yates.shared.model.ProteinBean;
import gnu.trove.map.hash.THashMap;

public class ServerCacheProteinBeansByProjectTag
		extends AbstractServerCacheForCollections<ProteinBean, List<ProteinBean>, String> {
	private static ServerCacheProteinBeansByProjectTag instance;

	public static ServerCacheProteinBeansByProjectTag getInstance() {
		if (instance == null) {
			instance = new ServerCacheProteinBeansByProjectTag();
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
