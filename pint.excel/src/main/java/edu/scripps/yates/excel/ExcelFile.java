package edu.scripps.yates.excel;

import java.util.List;
import java.util.Map;

public interface ExcelFile {
	public Map<String, ExcelSheet> getSheetMap();

	public List<ExcelSheet> getSheets();
}
