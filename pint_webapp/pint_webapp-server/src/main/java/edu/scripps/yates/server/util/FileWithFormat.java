package edu.scripps.yates.server.util;

import java.io.File;

import org.apache.commons.io.FilenameUtils;

import edu.scripps.yates.excel.proteindb.importcfg.jaxb.FormatType;
import edu.scripps.yates.shared.model.projectCreator.excel.FastaDigestionBean;

public class FileWithFormat {
	private final File file;
	private final FormatType format;
	private final String id;
	private final FastaDigestionBean fastaDigestionBean;

	public FileWithFormat(String id, File file, FormatType format,
			FastaDigestionBean fastaDigestionBean) {
		this.id = id;
		this.file = file;
		this.format = format;
		this.fastaDigestionBean = fastaDigestionBean;
	}

	/**
	 * Equals if same id
	 */
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof FileWithFormat) {
			FileWithFormat fileWithFormat = (FileWithFormat) obj;
			if (fileWithFormat.getId().equals(getId()))
				return true;

			return false;
		}
		return super.equals(obj);
	}

	/**
	 * @return the fileName
	 */
	public File getFile() {
		return file;
	}

	/**
	 * @return the fileType
	 */
	public FormatType getFormat() {
		return format;
	}

	public String getFileName() {
		return FilenameUtils.getName(file.getAbsolutePath());
	}

	public String getId() {
		return id;
	}

	/**
	 * @return the fastaDigestionBean
	 */
	public FastaDigestionBean getFastaDigestionBean() {
		return fastaDigestionBean;
	}

}
