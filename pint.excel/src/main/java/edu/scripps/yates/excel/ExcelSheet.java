package edu.scripps.yates.excel;

import java.util.List;
import java.util.Map;

public interface ExcelSheet {
	public String getName();

	public Map<String, ExcelColumn> getColumnMap();

	public ExcelColumn getColumn(String columnKey);

	public List<ExcelColumn> getColumns();

	public List<String> getColumnKeys();

	public List<String> getColumnHeaders();

	public List<ExcelColumn> getNumericalColumns();

	public List<ExcelColumn> getNonNumericalColumns();

	public int getSheetIndex();

}
