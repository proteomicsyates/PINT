package edu.scripps.yates.server.cache;

import java.util.Map;

import edu.scripps.yates.shared.util.FileDescriptor;
import gnu.trove.map.hash.THashMap;

public class ServerCacheProteinFileDescriptorByProjectName extends AbstractServerCache<FileDescriptor, String> {
	private static ServerCacheProteinFileDescriptorByProjectName instance;

	public static ServerCacheProteinFileDescriptorByProjectName getInstance() {
		if (instance == null) {
			instance = new ServerCacheProteinFileDescriptorByProjectName();
		}
		return instance;
	}

	@Override
	protected Map<String, FileDescriptor> createMap() {

		return new THashMap<String, FileDescriptor>();
	}
}
