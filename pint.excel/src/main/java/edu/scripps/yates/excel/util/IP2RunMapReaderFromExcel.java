package edu.scripps.yates.excel.util;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import edu.scripps.yates.excel.ExcelReader;
import gnu.trove.map.hash.THashMap;

public class IP2RunMapReaderFromExcel {
	private static final String inputFilePath = "C:\\Users\\Salva\\Desktop\\Dropbox\\Scripps\\Sandra\\new files\\paths_IP2.xls";
	private static final Map<String, String> map = new THashMap<String, String>();
	private static Logger log = Logger.getLogger(IP2RunMapReaderFromExcel.class);

	private IP2RunMapReaderFromExcel() {

	}

	/**
	 * @return the map
	 */
	public static Map<String, String> getMap() {
		if (map.isEmpty())
			loadData();
		return map;
	}

	/**
	 * @return the map
	 */
	public static String getPathByReplicateName(String replicateName) {
		if (map.isEmpty())
			loadData();
		return map.get(replicateName);
	}

	private static void loadData() {
		try {
			ExcelReader reader = new ExcelReader(inputFilePath, 0, 0);
			int rowNumber = 1;
			List<String> stringValues = reader.getStringValues(0, rowNumber);
			while (!stringValues.isEmpty()) {
				final String replicateName = stringValues.get(stringValues.size() - 2);
				final String path = stringValues.get(stringValues.size() - 1);
				if (replicateName != null && path != null) {
					// log.debug(replicateName + " " + path);
					map.put(replicateName, path);
				}
				stringValues = reader.getStringValues(0, ++rowNumber);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
}
