package edu.scripps.yates.server.projectCreator.adapter.excel2bean;

import edu.scripps.yates.excel.proteindb.importcfg.jaxb.SheetType;
import edu.scripps.yates.excel.proteindb.importcfg.jaxb.SheetsType;
import edu.scripps.yates.proteindb.persistence.mysql.adapter.Adapter;
import edu.scripps.yates.shared.model.projectCreator.excel.SheetsTypeBean;

public class SheetsTypeBeanAdapter implements Adapter<SheetsTypeBean> {
	private final SheetsType sheets;

	public SheetsTypeBeanAdapter(SheetsType sheets) {
		this.sheets = sheets;
	}

	@Override
	public SheetsTypeBean adapt() {

		return adaptFromSheetTypeBean();

	}

	private SheetsTypeBean adaptFromSheetTypeBean() {
		SheetsTypeBean ret = new SheetsTypeBean();
		for (SheetType sheetType : sheets.getSheet()) {
			ret.getSheet().add(new SheetTypeBeanAdapter(sheetType).adapt());
		}
		return ret;
	}

}
