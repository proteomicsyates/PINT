package edu.scripps.yates.client.cache;

import java.util.HashMap;
import java.util.Map;

import edu.scripps.yates.shared.util.FileDescriptor;

public class ClientCacheProteinGroupFileDescriptorByProjectName extends AbstractClientCache<FileDescriptor, String> {
	private static ClientCacheProteinGroupFileDescriptorByProjectName instance;

	public static ClientCacheProteinGroupFileDescriptorByProjectName getInstance() {
		if (instance == null) {
			instance = new ClientCacheProteinGroupFileDescriptorByProjectName();
		}
		return instance;
	}

	@Override
	protected Map<String, FileDescriptor> createMap() {
		return new HashMap<String, FileDescriptor>();
	}
}
