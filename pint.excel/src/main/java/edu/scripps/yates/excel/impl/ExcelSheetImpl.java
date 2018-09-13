package edu.scripps.yates.excel.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

import edu.scripps.yates.excel.ExcelColumn;
import edu.scripps.yates.excel.ExcelSheet;
import gnu.trove.map.hash.THashMap;
import gnu.trove.map.hash.TShortObjectHashMap;

public class ExcelSheetImpl implements ExcelSheet {
	private final static Logger log = Logger.getLogger(ExcelSheetImpl.class);

	private final String name;
	private final Map<String, ExcelColumn> columnMap = new THashMap<String, ExcelColumn>();

	private final List<String> columnKeys = new ArrayList<String>();
	private final List<String> columnHeaders = new ArrayList<String>();

	private final int sheetIndex;

	public ExcelSheetImpl(Sheet sheet, int sheetIndex) {
		name = sheet.getSheetName();
		this.sheetIndex = sheetIndex;
		final TShortObjectHashMap<String> columnIndexMap = new TShortObjectHashMap<String>();
		final int lastRowNum = sheet.getLastRowNum();
		for (int rowIndex = 0; rowIndex <= lastRowNum; rowIndex++) {
			final Row row = sheet.getRow(rowIndex);
			if (row != null) {
				if (rowIndex == 0) {
					// create the columns
					final short minColIx = row.getFirstCellNum();
					final short maxColIx = row.getLastCellNum();
					for (short colIx = minColIx; colIx < maxColIx; colIx++) {
						final Cell cell = row.getCell(colIx);
						if (cell == null) {
							continue;
						}
						final String headerName = cell.getStringCellValue();
						columnIndexMap.put(colIx, headerName);
						columnHeaders.add(headerName);
						final String columnKey = org.apache.poi.ss.util.CellReference.convertNumToColString(colIx);
						columnKeys.add(columnKey);

						final ExcelColumn excelColumn = new ExcelColumnImpl(columnKey, headerName);

						columnMap.put(columnKey, excelColumn);
					}
				} else {
					// data cells
					final short minColIx = row.getFirstCellNum();
					final short maxColIx = row.getLastCellNum();
					for (short colIx = minColIx; colIx < maxColIx; colIx++) {
						if (!columnIndexMap.containsKey(colIx))
							continue;
						final Cell cell = row.getCell(colIx);
						Object cellValue = null;
						if (cell != null) {
							cellValue = getCellValue(cell);
						}
						final String columnKey = org.apache.poi.ss.util.CellReference.convertNumToColString(colIx);
						final ExcelColumnImpl excelColumn = (ExcelColumnImpl) columnMap.get(columnKey);

						excelColumn.addData(rowIndex, cellValue);
					}
				}
			}
		}
		log.debug("Sheet '" + name + "' processed.");
		log.debug("Number of columns :" + columnMap.size());
		for (final String columnKey : columnKeys) {
			log.debug(columnKey + " " + columnMap.get(columnKey).getSize() + " values");
		}
	}

	private Object getCellValue(Cell cell) {
		if (cell != null) {
			switch (cell.getCellType()) {
			case Cell.CELL_TYPE_BLANK:
				return null;
			case Cell.CELL_TYPE_BOOLEAN:
				return Boolean.valueOf(cell.getBooleanCellValue());
			case Cell.CELL_TYPE_ERROR:
				return "";
			case Cell.CELL_TYPE_FORMULA:
				try {
					return Double.valueOf(cell.getStringCellValue());
				} catch (final NumberFormatException ex) {
					return cell.getStringCellValue();

				} catch (final IllegalStateException ex) {
					return cell.getNumericCellValue();

				}

			case Cell.CELL_TYPE_NUMERIC:
				try {
					return Double.valueOf(cell.getNumericCellValue());
				} catch (final NumberFormatException ex) {
					return cell.getStringCellValue();

				}
			case Cell.CELL_TYPE_STRING:
				try {
					return Double.valueOf(cell.getNumericCellValue());
				} catch (final NumberFormatException ex) {
					return cell.getStringCellValue();
				} catch (final IllegalStateException ex) {
					return cell.getStringCellValue();

				}

			default:
				break;
			}

		}
		return null;

	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public Map<String, ExcelColumn> getColumnMap() {
		return columnMap;
	}

	@Override
	public ExcelColumn getColumn(String columnKey) {
		return columnMap.get(columnKey);
	}

	@Override
	public List<ExcelColumn> getColumns() {
		final List<ExcelColumn> ret = new ArrayList<ExcelColumn>();
		for (final String columnKey : columnKeys) {
			ret.add(columnMap.get(columnKey));
		}
		return ret;
	}

	@Override
	public List<String> getColumnKeys() {
		return columnKeys;
	}

	@Override
	public List<ExcelColumn> getNumericalColumns() {
		final List<ExcelColumn> ret = new ArrayList<ExcelColumn>();
		for (final String columnKey : columnKeys) {
			final ExcelColumn excelColumn = columnMap.get(columnKey);
			if (excelColumn.isNumerical())
				ret.add(excelColumn);
		}
		return ret;
	}

	@Override
	public List<ExcelColumn> getNonNumericalColumns() {
		final List<ExcelColumn> ret = new ArrayList<ExcelColumn>();
		for (final String columnKey : columnKeys) {
			final ExcelColumn excelColumn = columnMap.get(columnKey);
			if (!excelColumn.isNumerical())
				ret.add(excelColumn);
		}
		return ret;
	}

	@Override
	public List<String> getColumnHeaders() {
		return columnHeaders;
	}

	@Override
	public int getSheetIndex() {
		return sheetIndex;
	}

}
