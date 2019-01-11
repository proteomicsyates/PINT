package edu.scripps.yates.server.util;

import edu.scripps.yates.excel.proteindb.importcfg.jaxb.FormatType;
import edu.scripps.yates.excel.proteindb.importcfg.jaxb.ServerType;
import edu.scripps.yates.shared.model.projectCreator.excel.FastaDigestionBean;
import edu.scripps.yates.utilities.remote.RemoteSSHFileReference;

public class RemoteFileWithType {
	private final RemoteSSHFileReference remoteFile;
	private final FormatType fileType;
	private final String id;
	private final ServerType server;
	private final FastaDigestionBean fastaDigestionBean;

	public RemoteFileWithType(String id, RemoteSSHFileReference remoteFile,
			FormatType fileType, ServerType server,
			FastaDigestionBean fastaDigestionBean) {
		this.id = id;
		this.remoteFile = remoteFile;
		this.fileType = fileType;
		this.server = server;
		this.fastaDigestionBean = fastaDigestionBean;
	}

	/**
	 * @return the fileName
	 */
	public RemoteSSHFileReference getRemoteFileSSHReference() {
		return remoteFile;
	}

	/**
	 * @return the fileType
	 */
	public FormatType getFormat() {
		return fileType;
	}

	public String getId() {
		return id;
	}

	public ServerType getServer() {
		return server;
	}

	/**
	 * @return the fastaDigestionBean
	 */
	public FastaDigestionBean getFastaDigestionBean() {
		return fastaDigestionBean;
	}

}
