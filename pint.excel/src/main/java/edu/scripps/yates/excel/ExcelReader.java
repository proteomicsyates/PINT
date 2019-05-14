package edu.scripps.yates.excel;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FilenameUtils;
import org.apache.log4j.Logger;
import org.apache.poi.POIXMLException;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Row.MissingCellPolicy;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import edu.scripps.yates.excel.util.ColumnIndexManager;
import edu.scripps.yates.utilities.files.FileUtils;
import gnu.trove.map.hash.THashMap;
import gnu.trove.map.hash.TIntObjectHashMap;

public class ExcelReader {
	private final String filePath;
	private TIntObjectHashMap<List<String>> columnNames = new TIntObjectHashMap<List<String>>();
	private Workbook wb;
	private ColumnIndexManager columnIndexManager;
	private final int minSheetIndex;
	private final int maxSheetIndex;
	private static Logger log = Logger.getLogger(ExcelReader.class);

	// use for distinguish replicates...and so on

	public enum ColumnType {
		DRUG_TREATMENT, EXPRESSION_ANALYSIS, FUNCTIONAL_ANNOTATION, ID, TEMPERATURE_SHIFT, WTF508_COMPARISON, ALL
	}

	public ExcelReader(File file, int minSheetIndex, int maxSheetIndex) throws IOException {
		this(file.getAbsolutePath(), minSheetIndex, maxSheetIndex);
	}

	public ExcelReader(String filePath, int minSheetIndex, int maxSheetIndex) throws IOException {
		this.minSheetIndex = minSheetIndex;
		this.maxSheetIndex = maxSheetIndex;
		this.filePath = filePath;
		final int[] sheetNumbers = getColumnNames(minSheetIndex, maxSheetIndex).keys();
		if (log.isDebugEnabled()) {
			for (final int sheetNum : sheetNumbers) {
				final List<String> list = getColumnNames(minSheetIndex, maxSheetIndex).get(sheetNum);
				for (final String string : list) {
					log.debug(string);
				}

			}
		}
		// ONLY FOR SANNI TABLE CFTR
		// columnIndexManager = new ColumnIndexManager(columnNames);
	}

	public File saveAsTXT(String separator) throws IOException {
		final File output = new File(new File(filePath).getParentFile().getAbsolutePath() + File.separator
				+ FilenameUtils.getBaseName(filePath) + ".txt");
		final FileWriter fw = new FileWriter(output);
		int rowNumber = 1;
		List<String> stringValues = getStringValues(0, rowNumber);
		while (!stringValues.isEmpty()) {
			for (final String value : stringValues) {
				String string = "";
				if (value != null) {
					string = value;
				}
				fw.write(string.trim());
				if (separator != null) {
					fw.write(separator);
				}
			}
			fw.write("\n");
			stringValues = getStringValues(0, ++rowNumber);
		}
		fw.close();
		return output;
	}

	public List<String> getStringValues(int sheetNumber, int rowNumber) throws IOException {
		final List<String> ret = new ArrayList<String>();

		final Workbook wb = getWorkbook();

		final Sheet sheet = wb.getSheetAt(sheetNumber);
		final Row row = sheet.getRow(rowNumber);
		if (row != null) {
			final int max = row.getLastCellNum();
			for (int i = 0; i < max; i++) {
				final Cell cell = row.getCell(i, Row.RETURN_NULL_AND_BLANK);
				if (cell != null) {
					switch (cell.getCellType()) {
					case Cell.CELL_TYPE_STRING:
						ret.add(cell.getStringCellValue());
						break;
					case Cell.CELL_TYPE_NUMERIC:
						if (DateUtil.isCellDateFormatted(cell)) {
							ret.add(cell.getDateCellValue().toString());
						} else {
							ret.add(String.valueOf(cell.getNumericCellValue()));
						}
						break;
					default:
						ret.add(null);
					}
				} else {
					ret.add(null);
				}
			}
		}

		return ret;
	}

	private Row getRow(int sheetNumber, int numRow) {
		try {
			return getWorkbook().getSheetAt(sheetNumber).getRow(numRow);
		} catch (final IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	public String getStringValue(int sheetNumber, int numRow, String colLetter) {
		return getStringValue(sheetNumber, numRow,
				org.apache.poi.ss.util.CellReference.convertColStringToIndex(colLetter));
	}

	public String getStringValue(int sheetNumber, int numRow, int numCol) {
		try {
			final Sheet sheetAt = getWorkbook().getSheetAt(sheetNumber);
			final Row row = sheetAt.getRow(numRow);
			if (row != null) {
				final Cell cell = row.getCell(numCol, Row.RETURN_BLANK_AS_NULL);
				if (cell == null) {
					return null;
				}

				try {
					return cell.getStringCellValue();
				} catch (final IllegalStateException e) {
					try {
						return String.valueOf(cell.getNumericCellValue());
					} catch (final IllegalStateException e2) {
						return String.valueOf(cell.getBooleanCellValue());
					}
				}
			}
		} catch (final IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	public String getNumberValue(int sheetNumber, int numRow, String colLetter) {
		return getNumberValue(sheetNumber, numRow,
				org.apache.poi.ss.util.CellReference.convertColStringToIndex(colLetter));
	}

	public String getNumberValue(int sheetNumber, int numRow, int numCol) {
		try {
			final Sheet sheetAt = getWorkbook().getSheetAt(sheetNumber);
			final Row row = sheetAt.getRow(numRow);
			if (row == null) {
				return null;
			}
			final Cell cell = row.getCell(numCol, Row.RETURN_BLANK_AS_NULL);
			if (cell == null) {
				return null;
			}
			try {
				final double numericCellValue = cell.getNumericCellValue();
				return String.valueOf(numericCellValue);
			} catch (final IllegalStateException e) {
				return cell.getStringCellValue();
			}
		} catch (final IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	public Map<String, String> getColumnValuePairs(int sheetNumber, int rowNumber) throws IOException {

		final Map<String, String> ret = new THashMap<String, String>();

		final List<String> columnNamesOfTheSheet = getColumnNames(sheetNumber, sheetNumber).get(sheetNumber);

		final Row row = getRow(sheetNumber, rowNumber);
		if (row != null) {

			for (int colIndex = 0; colIndex < columnNamesOfTheSheet.size(); colIndex++) {
				final Cell cell = row.getCell(colIndex);

				final String columnName = columnNamesOfTheSheet.get(colIndex);

				// System.out.println(columnName);
				if (cell != null) {
					switch (cell.getCellType()) {
					case Cell.CELL_TYPE_STRING:
						ret.put(columnName, cell.getStringCellValue());
						break;
					case Cell.CELL_TYPE_NUMERIC:
						try {
							if (DateUtil.isCellDateFormatted(cell)) {
								ret.put(columnName, cell.getDateCellValue().toString());
							} else {
								ret.put(columnName, String.valueOf(cell.getNumericCellValue()));
							}

						} catch (final Exception e) {
							ret.put(columnName, String.valueOf(cell.getNumericCellValue()));
						}
						break;
					default:
						ret.put(columnName, null);
					}
				} else {
					ret.put(columnName, null);
				}
			}

		}

		return ret;
	}

	/**
	 * Gets a list of the colum names (first row string values) mapped to the index
	 * of each sheet of the workbook
	 *
	 * @return
	 * @throws IOException
	 */
	public TIntObjectHashMap<List<String>> getColumnNames() throws IOException {
		return getColumnNames(minSheetIndex, maxSheetIndex);
	}

	private TIntObjectHashMap<List<String>> getColumnNames(int minSheetIndex, int maxSheetIndex) throws IOException {
		if (columnNames.isEmpty()) {
			columnNames = new TIntObjectHashMap<List<String>>();
			final int numberOfSheets = getWorkbook().getNumberOfSheets();
			int numColumns = 0;
			for (int sheetNumber = minSheetIndex; sheetNumber < numberOfSheets; sheetNumber++) {
				if (sheetNumber > maxSheetIndex)
					break;
				final List<String> columnNamesAtSheet = getStringValues(sheetNumber, 0);
				log.debug(columnNamesAtSheet.size() + " columns at sheet " + sheetNumber);
				columnNames.put(sheetNumber, columnNamesAtSheet);
				numColumns += columnNamesAtSheet.size();
			}
			log.debug(numColumns + " total columns detected in " + numberOfSheets + " sheets");
		}
		return columnNames;
	}

	public Workbook getWorkbook() throws IOException {
		if (wb == null) {
			wb = openWorkbook(filePath);
		}
		return wb;
	}

	/**
	 * Gets a {@link Workbook} object from a certain file path using a certain
	 * {@link MissingCellPolicy}
	 *
	 * @param filePath
	 * @param missingCellPolicy
	 * @return
	 */
	public static Workbook openWorkbook(String filePath, MissingCellPolicy missingCellPolicy) {
		log.debug("Opening workbook " + filePath + " ");
		InputStream input = null;
		Workbook wb = null;
		try {
			try {
				if (FilenameUtils.getExtension(filePath).equalsIgnoreCase("csv")) {
					final String excelFilePath = FilenameUtils.getFullPath(filePath)
							+ FilenameUtils.getBaseName(filePath) + ".xlsx";
					FileUtils.separatedValuesToXLSX(filePath, excelFilePath, ",");
					filePath = excelFilePath;
				} else if (FilenameUtils.getExtension(filePath).equalsIgnoreCase("tsv")) {
					final String excelFilePath = FilenameUtils.getFullPath(filePath)
							+ FilenameUtils.getBaseName(filePath) + ".xlsx";
					FileUtils.separatedValuesToXLSX(filePath, excelFilePath, "\t");
					filePath = excelFilePath;
				}
				input = new BufferedInputStream(new FileInputStream(filePath));
				wb = new XSSFWorkbook(input);
				wb.setMissingCellPolicy(missingCellPolicy);

			} catch (final POIXMLException e) {
				input = new BufferedInputStream(new FileInputStream(filePath));
				wb = new HSSFWorkbook(input);
				wb.setMissingCellPolicy(missingCellPolicy);
			}
		} catch (final FileNotFoundException e) {
			e.printStackTrace();
		} catch (final IOException e) {
			e.printStackTrace();
		} finally {
			if (input != null)
				try {
					input.close();
				} catch (final IOException e) {
					e.printStackTrace();
				}
		}

		return wb;
	}

	/**
	 * Gets a {@link Workbook} object from a certain file path with a
	 * RETURN_NULL_AND_BLANK {@link MissingCellPolicy}
	 *
	 * @param filePath
	 * @return
	 */
	public static Workbook openWorkbook(String filePath) {
		return openWorkbook(filePath, Row.RETURN_NULL_AND_BLANK);
	}

	/**
	 * @return the columnIndexManager
	 */
	public ColumnIndexManager getColumnIndexManager() {
		return columnIndexManager;
	}

	public int getColumnIndex(int sheetIndex, String string) throws IOException {
		final List<String> list = getColumnNames().get(sheetIndex);
		if (list != null) {
			return list.indexOf(string);
		}
		return -1;
	}

}
