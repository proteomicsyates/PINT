package edu.scripps.yates.server.cache;

import java.util.Map;
import java.util.Set;

import edu.scripps.yates.shared.model.OrganismBean;
import gnu.trove.map.hash.THashMap;
import gnu.trove.set.hash.THashSet;

public class ServerCacheOrganismBeansByProjectName
		extends AbstractServerCacheForCollections<OrganismBean, Set<OrganismBean>, String> {
	private static ServerCacheOrganismBeansByProjectName instance;

	public static ServerCacheOrganismBeansByProjectName getInstance() {
		if (instance == null) {
			instance = new ServerCacheOrganismBeansByProjectName();
		}
		return instance;
	}

	@Override
	protected Map<String, Set<OrganismBean>> createMap() {
		return new THashMap<String, Set<OrganismBean>>();
	}

	@Override
	protected Set<OrganismBean> createCollection() {
		return new THashSet<OrganismBean>();
	}

}
