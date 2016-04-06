package edu.scripps.yates.server.projectCreator.adapter.bean2excel;

import edu.scripps.yates.excel.proteindb.importcfg.jaxb.FormatType;
import edu.scripps.yates.proteindb.persistence.mysql.adapter.Adapter;
import edu.scripps.yates.shared.model.projectCreator.excel.FormatTypeBean;

public class FormatTypeAdapter implements Adapter<FormatType> {
	private final FormatTypeBean format;

	public FormatTypeAdapter(FormatTypeBean format) {
		this.format = format;
	}

	@Override
	public FormatType adapt() {
		return FormatType.valueOf(format.name());
	}

}
