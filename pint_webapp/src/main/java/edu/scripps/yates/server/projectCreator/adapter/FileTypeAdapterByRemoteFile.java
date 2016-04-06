package edu.scripps.yates.server.projectCreator.adapter;

import edu.scripps.yates.excel.proteindb.importcfg.jaxb.FileType;
import edu.scripps.yates.excel.proteindb.importcfg.jaxb.ObjectFactory;
import edu.scripps.yates.excel.proteindb.importcfg.jaxb.ServerType;
import edu.scripps.yates.proteindb.persistence.mysql.adapter.Adapter;
import edu.scripps.yates.server.util.RemoteFileWithType;

public class FileTypeAdapterByRemoteFile implements Adapter<FileType> {
	private final RemoteFileWithType remoteFile;

	public FileTypeAdapterByRemoteFile(RemoteFileWithType remoteFile) {
		this.remoteFile = remoteFile;
	}

	@Override
	public FileType adapt() {
		ObjectFactory factory = new ObjectFactory();

		FileType fileType = factory.createFileType();

		fileType.setFormat(remoteFile.getFormat());
		fileType.setId(remoteFile.getId());

		final ServerType server = remoteFile.getServer();
		if (server != null)
			fileType.setServerRef(server.getId());
		if (remoteFile.getRemoteFileSSHReference() != null) {
			fileType.setName(remoteFile.getRemoteFileSSHReference()
					.getRemoteFileName());
			fileType.setRelativePath(remoteFile.getRemoteFileSSHReference()
					.getRemotePath());

		}
		return fileType;
	}
}
