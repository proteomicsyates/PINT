package edu.scripps.yates.excel.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.HashMap;

import org.apache.log4j.Logger;

public class NewRunsMapReaderFromTxt {
	private static final File newPathFiles = new File(
			"C:\\Users\\Salva\\Desktop\\Dropbox\\Scripps\\Sandra\\DtaSelect_ReRun\\newPaths.txt");
	private static final File newPathFilesInServer = new File(
			"/home/salvador/PInt/data/newPaths.txt");

	private static final HashMap<String, String> map = new HashMap<String, String>();
	private static Logger log = Logger.getLogger(NewRunsMapReaderFromTxt.class);

	private NewRunsMapReaderFromTxt() {

	}

	/**
	 * @return the map
	 */
	public static HashMap<String, String> getMap() {
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
			FileInputStream fis = null;
			try {
				fis = new FileInputStream(newPathFiles);
			} catch (FileNotFoundException e) {
				fis = new FileInputStream(newPathFilesInServer);
			}
			BufferedReader br = new BufferedReader(new InputStreamReader(fis,
					Charset.forName("UTF-8")));
			String line;
			while ((line = br.readLine()) != null) {
				final String[] split = line.split("\t");
				String replicateName = split[0];
				String path = split[1];
				map.put(replicateName, path);

			}
			br.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
}
