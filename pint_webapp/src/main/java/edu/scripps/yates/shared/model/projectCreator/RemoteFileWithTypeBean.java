package edu.scripps.yates.shared.model.projectCreator;

import java.io.Serializable;

import edu.scripps.yates.shared.model.projectCreator.excel.ServerTypeBean;

public class RemoteFileWithTypeBean extends FileNameWithTypeBean implements
		Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 6875916329376943278L;

	private String remotePath;
	private ServerTypeBean server;

	public RemoteFileWithTypeBean() {

	}

	/**
	 * @return the server
	 */
	public ServerTypeBean getServer() {
		return server;
	}

	/**
	 * @param server
	 *            the server to set
	 */
	public void setServer(ServerTypeBean server) {
		this.server = server;
	}

	/**
	 * @return the remotePath
	 */
	public String getRemotePath() {
		return remotePath;
	}

	/**
	 * @param remotePath
	 *            the remotePath to set
	 */
	public void setRemotePath(String remotePath) {
		this.remotePath = remotePath;
	}

	@Override
	public boolean isValid() {
		if (remotePath == null || "".equals(remotePath))
			return false;
		if (server == null || !server.isValid())
			return false;
		if (super.getFileName() == null || "".equals(getFileName()))
			return false;

		return true;
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * edu.scripps.yates.shared.model.projectCreator.FileNameWithTypeBean#equals
	 * (java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof RemoteFileWithTypeBean) {
			RemoteFileWithTypeBean remoteFileNameWithType = (RemoteFileWithTypeBean) obj;
			if (remoteFileNameWithType.getServer() != null
					&& getServer() != null) {
				if (remoteFileNameWithType.getServer().equals(getServer())) {
					if (remoteFileNameWithType.getFileFormat() != getFileFormat()) {
						return false;
					}
					if (!remoteFileNameWithType.getId().equals(getId())) {
						return false;
					}
					if (!remoteFileNameWithType.getFileName().equals(
							getFileName())) {
						return false;
					}
					return true;
				}
			}
			return false;
		}
		return super.equals(obj);
	}

}
