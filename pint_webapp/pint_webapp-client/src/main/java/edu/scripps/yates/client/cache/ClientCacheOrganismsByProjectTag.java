package edu.scripps.yates.client.cache;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import edu.scripps.yates.shared.model.OrganismBean;

public class ClientCacheOrganismsByProjectTag
		extends AbstractClientCacheForCollections<OrganismBean, Set<OrganismBean>, String> {
	private static ClientCacheOrganismsByProjectTag instance;

	public static ClientCacheOrganismsByProjectTag getInstance() {
		if (instance == null) {
			instance = new ClientCacheOrganismsByProjectTag();
		}
		return instance;
	}

	@Override
	protected Set<OrganismBean> createCollection() {
		return new HashSet<OrganismBean>();
	}

	@Override
	protected Map<String, Set<OrganismBean>> createMap() {
		return new HashMap<String, Set<OrganismBean>>();
	}
}
