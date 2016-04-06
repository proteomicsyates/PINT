package edu.scripps.yates.server.projectCreator.adapter.bean2excel;

import java.io.IOException;
import java.util.List;

import edu.scripps.yates.excel.ExcelColumn;
import edu.scripps.yates.excel.ExcelSheet;
import edu.scripps.yates.excel.impl.ExcelFileImpl;
import edu.scripps.yates.excel.proteindb.importcfg.jaxb.ColumnType;
import edu.scripps.yates.excel.proteindb.importcfg.jaxb.ObjectFactory;
import edu.scripps.yates.excel.proteindb.importcfg.jaxb.SheetType;
import edu.scripps.yates.excel.proteindb.importcfg.jaxb.SheetsType;
import edu.scripps.yates.proteindb.persistence.mysql.adapter.Adapter;
import edu.scripps.yates.server.util.FileWithFormat;
import edu.scripps.yates.shared.model.projectCreator.excel.SheetTypeBean;
import edu.scripps.yates.shared.model.projectCreator.excel.SheetsTypeBean;
import edu.scripps.yates.shared.util.SharedConstants;

public class SheetsTypeAdapter implements Adapter<SheetsType> {
	private final FileWithFormat fileWithType;
	private final SheetsTypeBean sheets;

	public SheetsTypeAdapter(FileWithFormat fileWithType) {
		this.fileWithType = fileWithType;
		sheets = null;
	}

	public SheetsTypeAdapter(SheetsTypeBean sheets) {
		this.sheets = sheets;
		fileWithType = null;
	}

	@Override
	public SheetsType adapt() {
		if (fileWithType != null) {
			return adaptFromFileWithType();
		} else if (sheets != null) {
			return adaptFromSheetTypeBean();
		}
		return null;
	}

	private SheetsType adaptFromSheetTypeBean() {
		SheetsType ret = new SheetsType();
		for (SheetTypeBean sheetTypeBean : sheets.getSheet()) {
			ret.getSheet().add(new SheetTypeAdapter(sheetTypeBean).adapt());
		}
		return ret;
	}

	private SheetsType adaptFromFileWithType() {
		ObjectFactory factory = new ObjectFactory();
		SheetsType ret = factory.createSheetsType();

		try {
			ExcelFileImpl excelFile = new ExcelFileImpl(fileWithType.getFile());
			final List<ExcelSheet> sheets = excelFile.getSheets();
			for (ExcelSheet sheet : sheets) {
				SheetType sheetCfg = factory.createSheetType();
				sheetCfg.setId(fileWithType.getId()
						+ SharedConstants.EXCEL_ID_SEPARATOR + sheet.getName());
				final List<String> columnKeys = sheet.getColumnKeys();
				for (String columnKey : columnKeys) {
					ColumnType column = factory.createColumnType();
					final ExcelColumn excelColumn = sheet.getColumn(columnKey);
					column.setHeader(excelColumn.getHeader());
					column.setId(sheetCfg.getId()
							+ SharedConstants.EXCEL_ID_SEPARATOR
							+ excelColumn.getKey());
					column.setNumber(excelColumn.isNumerical());
					sheetCfg.getColumn().add(column);
				}
				ret.getSheet().add(sheetCfg);
			}

		} catch (IOException e) {
			e.printStackTrace();
			throw new IllegalArgumentException(e);
		}

		return ret;

	}

	private String getColumnId(Integer sheetNumber, int colNumber) {
		return getSheetId(sheetNumber) + "_" + colNumber;
	}

	private String getSheetId(Integer sheetNumber) {
		return fileWithType.getFileName() + "_Sheet" + sheetNumber;
	}
}
