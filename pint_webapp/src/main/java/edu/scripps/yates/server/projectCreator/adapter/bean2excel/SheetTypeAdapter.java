package edu.scripps.yates.server.projectCreator.adapter.bean2excel;

import edu.scripps.yates.excel.proteindb.importcfg.jaxb.SheetType;
import edu.scripps.yates.proteindb.persistence.mysql.adapter.Adapter;
import edu.scripps.yates.shared.model.projectCreator.excel.ColumnTypeBean;
import edu.scripps.yates.shared.model.projectCreator.excel.SheetTypeBean;

public class SheetTypeAdapter implements Adapter<SheetType> {
	private final SheetTypeBean sheetTypeBean;

	public SheetTypeAdapter(SheetTypeBean sheetTypeBean) {
		this.sheetTypeBean = sheetTypeBean;
	}

	@Override
	public SheetType adapt() {
		SheetType ret = new SheetType();
		ret.setId(sheetTypeBean.getId());
		for (ColumnTypeBean columnTypeBean : sheetTypeBean.getColumn()) {
			ret.getColumn().add(new ColumnTypeAdapter(columnTypeBean).adapt());
		}
		return ret;
	}

}
