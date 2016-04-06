package edu.scripps.yates.server.projectCreator.adapter.excel2bean;

import edu.scripps.yates.excel.proteindb.importcfg.jaxb.FormatType;
import edu.scripps.yates.proteindb.persistence.mysql.adapter.Adapter;
import edu.scripps.yates.shared.model.projectCreator.excel.FormatTypeBean;

public class FormatTypeBeanAdapter implements Adapter<FormatTypeBean> {
	private final FormatType format;

	public FormatTypeBeanAdapter(FormatType format) {
		this.format = format;
	}

	@Override
	public FormatTypeBean adapt() {
		if (format != null)
			return FormatTypeBean.fromValue(format.name());
		return null;
	}

}
