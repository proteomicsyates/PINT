package edu.scripps.yates.shared.model.projectCreator;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import edu.scripps.yates.shared.model.projectCreator.excel.MsRunTypeBean;

public class QuantificationRemoteFileBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3902989892887516192L;
	private List<RemoteFileWithTypeBean> remoteFiles = new ArrayList<RemoteFileWithTypeBean>();
	private List<FileNameWithTypeBean> localFiles = new ArrayList<FileNameWithTypeBean>();
	private MsRunTypeBean msRun;
	private String decoyRegexp;

	/**
	 * @return the decoyRegexp
	 */
	public String getDecoyRegexp() {
		return decoyRegexp;
	}

	/**
	 * @param decoyRegexp
	 *            the decoyRegexp to set
	 */
	public void setDecoyRegexp(String decoyRegexp) {
		this.decoyRegexp = decoyRegexp;
	}

	/**
	 * @return the remoteFiles
	 */
	public List<RemoteFileWithTypeBean> getRemoteFiles() {
		return remoteFiles;
	}

	/**
	 * @param remoteFiles
	 *            the remoteFiles to set
	 */
	public void setRemoteFiles(List<RemoteFileWithTypeBean> remoteFiles) {
		this.remoteFiles = remoteFiles;
	}

	public void addRemoteFile(RemoteFileWithTypeBean remoteFile) {
		if (remoteFiles == null)
			remoteFiles = new ArrayList<RemoteFileWithTypeBean>();
		remoteFiles.add(remoteFile);
	}

	/**
	 * @return the localFiles
	 */
	public List<FileNameWithTypeBean> getLocalFiles() {
		return localFiles;
	}

	/**
	 * @param localFiles
	 *            the localFiles to set
	 */
	public void setLocalFiles(List<FileNameWithTypeBean> localFiles) {
		this.localFiles = localFiles;
	}

	public void addLocalFile(FileNameWithTypeBean localFile) {
		if (localFiles == null)
			localFiles = new ArrayList<FileNameWithTypeBean>();
		localFiles.add(localFile);
	}

	/**
	 * @return the msRun
	 */
	public MsRunTypeBean getMsRun() {
		return msRun;
	}

	/**
	 * @param msRun
	 *            the msRun to set
	 */
	public void setMsRun(MsRunTypeBean msRun) {
		this.msRun = msRun;
	}
}
