package edu.scripps.yates.server.projectCreator.adapter;

import edu.scripps.yates.proteindb.persistence.mysql.adapter.Adapter;
import edu.scripps.yates.shared.model.projectCreator.RemoteFileWithTypeBean;
import edu.scripps.yates.utilities.remote.RemoteSSHFileReference;

public class RemoteSSHFileReferenceAdapter implements
		Adapter<RemoteSSHFileReference> {
	private final RemoteFileWithTypeBean remoteFile;

	public RemoteSSHFileReferenceAdapter(RemoteFileWithTypeBean remoteFile) {
		this.remoteFile = remoteFile;
	}

	@Override
	public RemoteSSHFileReference adapt() {

		RemoteSSHFileReference ret = new RemoteSSHFileReference();
		ret.setRemoteFileName(remoteFile.getFileName());
		ret.setRemotePath(remoteFile.getRemotePath());

		if (remoteFile.getServer() != null) {
			ret.setHostName(remoteFile.getServer().getHostName());
			ret.setPass(remoteFile.getServer().getPassword());
			ret.setUserName(remoteFile.getServer().getUserName());
		}
		return ret;

	}

}
