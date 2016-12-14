package edu.scripps.yates.shared.model;

import java.io.Serializable;

import edu.scripps.yates.shared.model.interfaces.HasId;
import edu.scripps.yates.shared.model.projectCreator.excel.FastaDigestionBean;
import edu.scripps.yates.shared.model.projectCreator.excel.ServerTypeBean;

public class DataSourceBean implements Serializable, HasId {
	/**
	 * 
	 */
	private static final long serialVersionUID = -6836950993637266393L;
	private String id;
	private String fileName;
	private String relativePath;
	private ServerTypeBean server;
	private FileFormat format;
	private String url;
	private FastaDigestionBean fastaDigestionBean;

	/**
	 * @return the id
	 */
	@Override
	public String getId() {
		return id;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * @return the fileName
	 */
	public String getFileName() {
		return fileName;
	}

	/**
	 * @param fileName
	 *            the fileName to set
	 */
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	/**
	 * @return the relativePath
	 */
	public String getRelativePath() {
		return relativePath;
	}

	/**
	 * @param relativePath
	 *            the relativePath to set
	 */
	public void setRelativePath(String relativePath) {
		this.relativePath = relativePath;
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
	 * @return the format
	 */
	public FileFormat getFormat() {
		return format;
	}

	/**
	 * @param format
	 *            the format to set
	 */
	public void setFormat(FileFormat format) {
		this.format = format;
	}

	/**
	 * @return the url
	 */
	public String getUrl() {
		return url;
	}

	/**
	 * @param url
	 *            the url to set
	 */
	public void setUrl(String url) {
		this.url = url;
	}

	/**
	 * @return the fastaDigestionBean
	 */
	public FastaDigestionBean getFastaDigestionBean() {
		return fastaDigestionBean;
	}

	/**
	 * @param fastaDigestionBean
	 *            the fastaDigestionBean to set
	 */
	public void setFastaDigestionBean(FastaDigestionBean fastaDigestionBean) {
		this.fastaDigestionBean = fastaDigestionBean;
	}

}
