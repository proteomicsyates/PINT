package edu.scripps.yates.server.cache;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import edu.scripps.yates.shared.model.PSMBean;
import gnu.trove.map.hash.THashMap;

public class ServerCachePSMBeansByProjectTag extends AbstractServerCacheForCollections<PSMBean, List<PSMBean>, String> {

	private static ServerCachePSMBeansByProjectTag instance;

	public static ServerCachePSMBeansByProjectTag getInstance() {
		if (instance == null) {
			instance = new ServerCachePSMBeansByProjectTag();
		}
		return instance;
	}

	@Override
	protected List<PSMBean> createCollection() {

		return new ArrayList<PSMBean>();
	}

	@Override
	protected Map<String, List<PSMBean>> createMap() {

		return new THashMap<String, List<PSMBean>>();
	}
}
