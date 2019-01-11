package edu.scripps.yates.server.cache;

import java.util.Map;

import edu.scripps.yates.shared.util.FileDescriptor;
import gnu.trove.map.hash.THashMap;

public class ServerCacheProteinFileDescriptorByClientIDAndQuery extends AbstractServerCache<FileDescriptor, String> {
	private static ServerCacheProteinFileDescriptorByClientIDAndQuery instance;

	public static ServerCacheProteinFileDescriptorByClientIDAndQuery getInstance() {
		if (instance == null) {
			instance = new ServerCacheProteinFileDescriptorByClientIDAndQuery();
		}
		return instance;
	}

	@Override
	protected Map<String, FileDescriptor> createMap() {
		return new THashMap<String, FileDescriptor>();
	}
}
