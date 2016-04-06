package edu.scripps.yates.shared.model.projectCreator;

import java.io.Serializable;

import edu.scripps.yates.shared.model.FileFormat;
import edu.scripps.yates.shared.model.projectCreator.excel.FastaDigestionBean;

public class FileNameWithTypeBean implements Serializable {
	/**
	 *
	 */
	private static final long serialVersionUID = 287747036476994272L;
	private String fileName;
	private FileFormat fileFormat;
	private String id;
	private FastaDigestionBean fastaDigestionBean;

	public FileNameWithTypeBean() {

	}

	/**
	 * @return the fileName
	 */
	public String getFileName() {
		return fileName;
	}

	/**
	 * @return the fileFormat
	 */
	public FileFormat getFileFormat() {
		return fileFormat;
	}

	/**
	 * @param fileName
	 *            the fileName to set
	 */
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	/**
	 * @param fileFormat
	 *            the fileFormat to set
	 */
	public void setFileFormat(FileFormat fileFormat) {
		this.fileFormat = fileFormat;
	}

	/**
	 * @return the id
	 */
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

	public boolean isValid() {
		if (id == null || "".equals(id))
			return false;
		if (fileFormat == null)
			return false;
		if (fileName == null || "".equals(fileFormat))
			return false;

		return true;
	}

	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof FileNameWithTypeBean) {
			FileNameWithTypeBean fileNameWithTypeBean = (FileNameWithTypeBean) obj;
			if (fileNameWithTypeBean.getFileFormat() != getFileFormat()) {
				return false;
			}
			if (!fileNameWithTypeBean.getId().equals(getId())) {
				return false;
			}
			if (!fileNameWithTypeBean.getFileName().equals(getFileName())) {
				return false;
			}
			if (fileNameWithTypeBean.getFastaDigestionBean() != null
					&& !fileNameWithTypeBean.getFastaDigestionBean().equals(getFastaDigestionBean()))
				return false;
			if (fileNameWithTypeBean.getFastaDigestionBean() == null && getFastaDigestionBean() != null)
				return false;
			return true;
		}
		return super.equals(obj);
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
