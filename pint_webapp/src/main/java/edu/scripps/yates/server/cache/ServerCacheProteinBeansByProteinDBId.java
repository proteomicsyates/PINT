package edu.scripps.yates.server.cache;

import edu.scripps.yates.shared.model.ProteinBean;
import gnu.trove.map.hash.TIntObjectHashMap;

public class ServerCacheProteinBeansByProteinDBId extends AbstractServerCacheByInteger<ProteinBean> {

	private static ServerCacheProteinBeansByProteinDBId instance;

	public static ServerCacheProteinBeansByProteinDBId getInstance() {
		if (instance == null) {
			instance = new ServerCacheProteinBeansByProteinDBId();
		}
		return instance;
	}

	@Override
	protected TIntObjectHashMap<ProteinBean> createMap() {
		return new TIntObjectHashMap<ProteinBean>();
	}

}
