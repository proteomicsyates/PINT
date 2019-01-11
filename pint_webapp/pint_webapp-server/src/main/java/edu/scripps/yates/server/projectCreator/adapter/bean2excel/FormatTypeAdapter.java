package edu.scripps.yates.server.projectCreator.adapter.bean2excel;

import edu.scripps.yates.excel.proteindb.importcfg.jaxb.FormatType;
import edu.scripps.yates.proteindb.persistence.mysql.adapter.Adapter;
import edu.scripps.yates.shared.model.FileFormat;

public class FormatTypeAdapter implements Adapter<FormatType> {
	private final FileFormat format;

	public FormatTypeAdapter(FileFormat format) {
		this.format = format;
	}

	@Override
	public FormatType adapt() {
		return FormatType.valueOf(format.name());
	}

}
