package edu.scripps.yates.excel.proteindb.importcfg;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
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
import edu.scripps.yates.excel.proteindb.importcfg.jaxb.ServerType;
import edu.scripps.yates.excel.proteindb.importcfg.jaxb.ServersType;
import edu.scripps.yates.utilities.remote.RemoteSSHFileReference;
import gnu.trove.map.hash.THashMap;

public class ExcelFileReader {
	private final Map<String, ExcelFile> excelFileMap = new THashMap<String, ExcelFile>();
	private final String errorMessage = "Column reference not recognized. It must be like 'fileName_sheetName_columnLetter'";

	public ExcelFileReader(FileSetType fileSet, ServersType servers) throws IOException, URISyntaxException {

		final List<FileType> files = fileSet.getFile();
		for (FileType file : files) {
			if (file.getFormat() == FormatType.EXCEL) {
				String id = file.getId();
				final String url = file.getUrl();
				File actualfile = null;
				if (url != null) {
					final URI uri = new URI(url);
					final String file2 = URIUtil.decode(uri.toURL().getFile());
					actualfile = new File(file2);
				} else {
					ServerType server = getServer(file.getServerRef(), servers);
					if (server == null) {
						throw new IllegalArgumentException("Server '" + file.getServerRef() + "' is not found");
					}
					RemoteSSHFileReference ssh = new RemoteSSHFileReference(server.getHostName(), server.getUserName(),
							server.getPassword(), file.getName(), File.createTempFile(file.getName(), ".tmp"));
					ssh.setRemotePath(file.getRelativePath());
					actualfile = ssh.getRemoteFile();
				}
				ExcelFile excelFile = new ExcelFileImpl(actualfile);
				excelFileMap.put(id, excelFile);
			}
		}
	}

	private ServerType getServer(String serverRef, ServersType servers) {
		for (ServerType server : servers.getServer()) {
			if (server.getId().equals(serverRef)) {
				return server;
			}
		}
		return null;
	}

	public ExcelColumn getExcelColumnFromReference(String columnReference) {
		// parse ColumnReference
		String fileId = getFileId(columnReference);
		if (fileId == null)
			throw new IllegalArgumentException("'" + columnReference + "' " + errorMessage);
		String sheetName = getSheetName(columnReference);
		if (sheetName == null)
			throw new IllegalArgumentException("'" + columnReference + "' " + errorMessage);
		String columnKey = getColumnKey(columnReference);
		if (columnKey == null)
			throw new IllegalArgumentException("'" + columnReference + "' " + errorMessage);

		if (!excelFileMap.containsKey(fileId)) {
			throw new IllegalArgumentException("File id '" + fileId + "' not found in import cfg file");
		}
		final ExcelFile excelFile = excelFileMap.get(fileId);
		if (!excelFile.getSheetMap().containsKey(sheetName)) {
			throw new IllegalArgumentException("Sheet '" + sheetName + "' not found in Excel file id:'" + fileId + "'");
		}
		final ExcelSheet excelSheet = excelFile.getSheetMap().get(sheetName);
		if (!excelSheet.getColumnMap().containsKey(columnKey)) {
			throw new IllegalArgumentException("Column '" + columnKey + "' not found in Sheet '" + sheetName
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
