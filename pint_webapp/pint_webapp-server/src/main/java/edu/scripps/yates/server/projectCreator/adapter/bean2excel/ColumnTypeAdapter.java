package edu.scripps.yates.server.projectCreator.adapter.bean2excel;

import edu.scripps.yates.excel.proteindb.importcfg.jaxb.ColumnType;
import edu.scripps.yates.proteindb.persistence.mysql.adapter.Adapter;
import edu.scripps.yates.shared.model.projectCreator.excel.ColumnTypeBean;

public class ColumnTypeAdapter implements Adapter<ColumnType> {
	private final ColumnTypeBean columnTypeBean;

	public ColumnTypeAdapter(ColumnTypeBean columnTypeBean) {
		this.columnTypeBean = columnTypeBean;
	}

	@Override
	public ColumnType adapt() {
		ColumnType ret = new ColumnType();
		ret.setHeader(columnTypeBean.getHeader());
		ret.setId(columnTypeBean.getId());
		ret.setNumber(columnTypeBean.isNumber());
		return ret;
	}

}
