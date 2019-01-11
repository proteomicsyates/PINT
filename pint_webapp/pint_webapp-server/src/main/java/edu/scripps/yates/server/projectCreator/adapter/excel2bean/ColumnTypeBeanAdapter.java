package edu.scripps.yates.server.projectCreator.adapter.excel2bean;

import edu.scripps.yates.excel.proteindb.importcfg.jaxb.ColumnType;
import edu.scripps.yates.proteindb.persistence.mysql.adapter.Adapter;
import edu.scripps.yates.shared.model.projectCreator.excel.ColumnTypeBean;

public class ColumnTypeBeanAdapter implements Adapter<ColumnTypeBean> {
	private final ColumnType columnType;

	public ColumnTypeBeanAdapter(ColumnType columnType) {
		this.columnType = columnType;
	}

	@Override
	public ColumnTypeBean adapt() {
		ColumnTypeBean ret = new ColumnTypeBean();
		ret.setHeader(columnType.getHeader());
		ret.setId(columnType.getId());
		ret.setNumber(columnType.isNumber());
		return ret;
	}

}
