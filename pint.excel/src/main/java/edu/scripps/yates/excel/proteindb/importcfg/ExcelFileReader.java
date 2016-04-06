package edu.scripps.yates.excel.proteindb.importcfg;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.httpclient.util.URIUtil;

import edu.scripps.yates.excel.ExcelColumn;
import edu.scripps.yates.excel.ExcelFile;
import edu.scripps.yates.excel.ExcelSheet;
import edu.scripps.yates.excel.impl.ExcelFileImpl;
import edu.scripps.yates.excel.proteindb.importcfg.jaxb.ColumnType;
import edu.scripps.yates.excel.proteindb.importcfg.jaxb.FileSetType;
import edu.scripps.yates.excel.proteindb.importcfg.jaxb.FileType;
import edu.scripps.yates.excel.proteindb.importcfg.jaxb.FormatType;

public class ExcelFileReader {
	private final Map<String, ExcelFile> excelFileMap = new HashMap<String, ExcelFile>();
	private final String errorMessage = "Column reference not recognized. It must be like 'fileName_sheetName_columnLetter'";

	public ExcelFileReader(FileSetType fileSet) throws IOException,
			URISyntaxException {

		final List<FileType> files = fileSet.getFile();
		for (FileType file : files) {
			if (file.getFormat() == FormatType.EXCEL) {
				String id = file.getId();
				final String url = file.getUrl();
				final URI uri = new URI(url);
				final String file2 = URIUtil.decode(uri.toURL().getFile());
				File actualfile = new File(file2);
				ExcelFile excelFile = new ExcelFileImpl(actualfile);
				excelFileMap.put(id, excelFile);
			}
		}
	}

	public ExcelColumn getExcelColumnFromReference(String columnReference) {
		// parse ColumnReference
		String fileId = getFileId(columnReference);
		if (fileId == null)
			throw new IllegalArgumentException("'" + columnReference + "' "
					+ errorMessage);
		String sheetName = getSheetName(columnReference);
		if (sheetName == null)
			throw new IllegalArgumentException("'" + columnReference + "' "
					+ errorMessage);
		String columnKey = getColumnKey(columnReference);
		if (columnKey == null)
			throw new IllegalArgumentException("'" + columnReference + "' "
					+ errorMessage);

		if (!excelFileMap.containsKey(fileId)) {
			throw new IllegalArgumentException("File id '" + fileId
					+ "' not found in import cfg file");
		}
		final ExcelFile excelFile = excelFileMap.get(fileId);
		if (!excelFile.getSheetMap().containsKey(sheetName)) {
			throw new IllegalArgumentException("Sheet '" + sheetName
					+ "' not found in Excel file id:'" + fileId + "'");
		}
		final ExcelSheet excelSheet = excelFile.getSheetMap().get(sheetName);
		if (!excelSheet.getColumnMap().containsKey(columnKey)) {
			throw new IllegalArgumentException("Column '" + columnKey
					+ "' not found in Sheet '" + sheetName
					+ "' from Excel file id:'" + fileId + "'");
		}
		return excelSheet.getColumn(columnKey);
	}

	public ExcelColumn getExcelColumnFromReference(ColumnType columnType) {
		return getExcelColumnFromReference(columnType.getId());
	}

	private static String getColumnKey(String columnReference) {
		String regexp = ".+##.+##(.+)";
		final Pattern compile = Pattern.compile(regexp);
		final Matcher matcher = compile.matcher(columnReference);
		if (matcher.find()) {
			return matcher.group(1);
		}
		return null;
	}

	private static String getSheetName(String columnReference) {
		String regexp = ".+##(.+)##.+";
		final Pattern compile = Pattern.compile(regexp);
		final Matcher matcher = compile.matcher(columnReference);
		if (matcher.find()) {
			return matcher.group(1);
		}
		return null;
	}

	private static String getFileId(String columnReference) {
		String regexp = "(.+)##.+##.+";
		final Pattern compile = Pattern.compile(regexp);
		final Matcher matcher = compile.matcher(columnReference);
		if (matcher.find()) {
			return matcher.group(1);
		}
		return null;
	}

}
