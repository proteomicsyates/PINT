package edu.scripps.yates.server.projectCreator.adapter.excel2bean;

import edu.scripps.yates.excel.proteindb.importcfg.jaxb.FormatType;
import edu.scripps.yates.proteindb.persistence.mysql.adapter.Adapter;
import edu.scripps.yates.shared.model.FileFormat;

public class FormatTypeBeanAdapter implements Adapter<FileFormat> {
	private final FormatType format;

	public FormatTypeBeanAdapter(FormatType format) {
		this.format = format;
	}

	@Override
	public FileFormat adapt() {
		if (format != null)
			return FileFormat.getFileFormatFromString(format.name());
		return null;
	}

}
