package edu.scripps.yates.server.cache;

import edu.scripps.yates.shared.model.PSMBean;
import gnu.trove.map.hash.TIntObjectHashMap;

public class ServerCachePSMBeansByPSMDBId extends AbstractServerCacheByInteger<PSMBean> {

	private static ServerCachePSMBeansByPSMDBId instance;

	public static ServerCachePSMBeansByPSMDBId getInstance() {
		if (instance == null) {
			instance = new ServerCachePSMBeansByPSMDBId();
		}
		return instance;
	}

	@Override
	protected TIntObjectHashMap<PSMBean> createMap() {
		return new TIntObjectHashMap<PSMBean>();
	}
}
