package edu.scripps.yates.client.cache;

import java.util.HashMap;
import java.util.Map;

import edu.scripps.yates.shared.util.FileDescriptor;

public class ClientCacheInputDataFilesZipByProjectName extends AbstractClientCache<FileDescriptor, String> {
	private static ClientCacheInputDataFilesZipByProjectName instance;

	public static ClientCacheInputDataFilesZipByProjectName getInstance() {
		if (instance == null) {
			instance = new ClientCacheInputDataFilesZipByProjectName();
		}
		return instance;
	}

	@Override
	protected Map<String, FileDescriptor> createMap() {
		return new HashMap<String, FileDescriptor>();
	}
}
