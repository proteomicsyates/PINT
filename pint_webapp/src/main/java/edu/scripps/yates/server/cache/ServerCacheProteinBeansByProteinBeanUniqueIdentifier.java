package edu.scripps.yates.server.cache;

import edu.scripps.yates.shared.model.ProteinBean;
import gnu.trove.map.hash.TIntObjectHashMap;

public class ServerCacheProteinBeansByProteinBeanUniqueIdentifier extends AbstractServerCacheByInteger<ProteinBean> {

	private static ServerCacheProteinBeansByProteinBeanUniqueIdentifier instance;

	public static ServerCacheProteinBeansByProteinBeanUniqueIdentifier getInstance() {
		if (instance == null) {
			instance = new ServerCacheProteinBeansByProteinBeanUniqueIdentifier();
		}
		return instance;
	}

	@Override
	protected TIntObjectHashMap<ProteinBean> createMap() {
		return new TIntObjectHashMap<ProteinBean>();
	}
}
