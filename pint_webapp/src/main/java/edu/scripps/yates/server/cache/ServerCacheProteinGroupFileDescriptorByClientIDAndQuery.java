package edu.scripps.yates.server.cache;

import java.util.Map;

import edu.scripps.yates.shared.util.FileDescriptor;
import gnu.trove.map.hash.THashMap;

public class ServerCacheProteinGroupFileDescriptorByClientIDAndQuery
		extends AbstractServerCache<FileDescriptor, String> {
	private static ServerCacheProteinGroupFileDescriptorByClientIDAndQuery instance;

	public static ServerCacheProteinGroupFileDescriptorByClientIDAndQuery getInstance() {
		if (instance == null) {
			instance = new ServerCacheProteinGroupFileDescriptorByClientIDAndQuery();
		}
		return instance;
	}

	@Override
	protected Map<String, FileDescriptor> createMap() {
		return new THashMap<String, FileDescriptor>();
	}
}
