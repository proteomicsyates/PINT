package edu.scripps.yates.client.cache;

import java.util.HashMap;
import java.util.Map;

import edu.scripps.yates.shared.util.FileDescriptor;

public class ClientCacheProteinFileDescriptorByProjectName extends AbstractClientCache<FileDescriptor, String> {
	private static ClientCacheProteinFileDescriptorByProjectName instance;

	public static ClientCacheProteinFileDescriptorByProjectName getInstance() {
		if (instance == null) {
			instance = new ClientCacheProteinFileDescriptorByProjectName();
		}
		return instance;
	}

	@Override
	protected Map<String, FileDescriptor> createMap() {
		return new HashMap<String, FileDescriptor>();
	}
}
