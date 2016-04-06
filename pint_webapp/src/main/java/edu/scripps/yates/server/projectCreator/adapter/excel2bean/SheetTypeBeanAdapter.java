package edu.scripps.yates.server.projectCreator.adapter.excel2bean;

import edu.scripps.yates.excel.proteindb.importcfg.jaxb.ColumnType;
import edu.scripps.yates.excel.proteindb.importcfg.jaxb.SheetType;
import edu.scripps.yates.proteindb.persistence.mysql.adapter.Adapter;
import edu.scripps.yates.shared.model.projectCreator.excel.SheetTypeBean;

public class SheetTypeBeanAdapter implements Adapter<SheetTypeBean> {
	private final SheetType sheetType;

	public SheetTypeBeanAdapter(SheetType sheetType) {
		this.sheetType = sheetType;
	}

	@Override
	public SheetTypeBean adapt() {
		SheetTypeBean ret = new SheetTypeBean();
		ret.setId(sheetType.getId());
		for (ColumnType columnType : sheetType.getColumn()) {
			ret.getColumn().add(new ColumnTypeBeanAdapter(columnType).adapt());
		}
		return ret;
	}

}
