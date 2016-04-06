package edu.scripps.yates.excel.impl;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import edu.scripps.yates.excel.ExcelFile;
import edu.scripps.yates.excel.ExcelReader;
import edu.scripps.yates.excel.ExcelSheet;

public class ExcelFileImpl implements ExcelFile {
	private final static Logger log = Logger.getLogger(ExcelFileImpl.class);
	private final Map<String, ExcelSheet> sheetMap = new HashMap<String, ExcelSheet>();
	private final List<String> sheetKeys = new ArrayList<String>();

	public ExcelFileImpl(File file) throws IOException {
		if (!file.exists())
			throw new IllegalArgumentException("File doesn't exist");
		ExcelReader reader = new ExcelReader(file.getAbsolutePath(), 0,
				Integer.MAX_VALUE);
		final Workbook workbook = reader.getWorkbook();
		final int numberOfSheets = workbook.getNumberOfSheets();
		for (int numSheet = 0; numSheet < numberOfSheets; numSheet++) {
			final Sheet sheet = workbook.getSheetAt(numSheet);
			final String sheetName = sheet.getSheetName();
			sheetKeys.add(sheetName);
			if (sheetMap.containsKey(sheetName))
				throw new IllegalArgumentException("Excel file '"
						+ file.getAbsolutePath()
						+ "' contains duplicated sheet names");
			ExcelSheet excelSheet = new ExcelSheetImpl(sheet, numSheet);
			sheetMap.put(sheetName, excelSheet);
		}
		log.info("Excel file '" + file.getAbsolutePath() + "' processed.");
		log.info("Number of sheets :" + sheetMap.size());
	}

	@Override
	public Map<String, ExcelSheet> getSheetMap() {
		return sheetMap;
	}

	@Override
	public List<ExcelSheet> getSheets() {
		List<ExcelSheet> ret = new ArrayList<ExcelSheet>();
		for (String sheetKey : sheetKeys) {
			ret.add(sheetMap.get(sheetKey));
		}
		return ret;
	}

}
