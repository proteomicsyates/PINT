package edu.scripps.yates.server.cache;

import java.util.Map;

import edu.scripps.yates.shared.util.FileDescriptor;
import gnu.trove.map.hash.THashMap;

public class ServerCacheProteinGroupFileDescriptorByProjectName extends AbstractServerCache<FileDescriptor, String> {
	private static ServerCacheProteinGroupFileDescriptorByProjectName instance;

	public static ServerCacheProteinGroupFileDescriptorByProjectName getInstance() {
		if (instance == null) {
			instance = new ServerCacheProteinGroupFileDescriptorByProjectName();
		}
		return instance;
	}

	@Override
	protected Map<String, FileDescriptor> createMap() {
		return new THashMap<String, FileDescriptor>();
	}
}
