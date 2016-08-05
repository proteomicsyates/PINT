package edu.scripps.yates.census.read.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.io.FilenameUtils;

public class FileSplitter {
	/**
	 * Reads the relatFile and split the file into as many dataSetNames as
	 * exist, each one containing the data corresponding to each dataSet
	 *
	 * @param inputFile
	 * @param dataSetNames
	 * @return
	 * @throws IOException
	 */
	public static Map<String, File> splitFiles(File inputFile, Collection<String> dataSetNames) throws IOException {

		// if (dataSetNames.size() == 1) {
		// Map<String, File> ret = new HashMap<String, File>();
		// ret.put(dataSetNames.iterator().next(), inputFile);
		// return ret;
		// }
		Map<String, BufferedWriter> mapOfFiles = new HashMap<String, BufferedWriter>();
		Map<String, File> listOfFiles = new HashMap<String, File>();
		// create an output file per each dataSetName
		for (String dataSetName : dataSetNames) {
			File file = new File(inputFile.getParentFile().getAbsoluteFile() + File.separator
					+ FilenameUtils.getBaseName(inputFile.getAbsolutePath()) + "_" + dataSetName + "."
					+ FilenameUtils.getExtension(inputFile.getAbsolutePath()));
			listOfFiles.put(dataSetName, file);
			FileWriter fstream = new FileWriter(file);
			BufferedWriter out = new BufferedWriter(fstream);
			mapOfFiles.put(dataSetName, out);
		}

		FileInputStream fis;
		try {
			fis = new FileInputStream(inputFile);
			BufferedReader in = new BufferedReader(new InputStreamReader(fis));

			String aLine;
			String firstLine = null;
			while ((aLine = in.readLine()) != null) {
				if (firstLine == null) {
					firstLine = aLine;
					// write the first line in all the writters
					Collection<BufferedWriter> outs = mapOfFiles.values();
					for (BufferedWriter out : outs) {
						out.write(firstLine);
						out.newLine();
					}
					continue;
				}
				final String[] split = aLine.split("\t");
				for (String dataSetName : dataSetNames) {
					if (split[0].contains(dataSetName) || split[1].contains(dataSetName)) {
						// get the file writter corresponding to the dataset
						// detected as sufix
						BufferedWriter out = mapOfFiles.get(dataSetName);
						out.write(aLine);
						out.newLine();
					}
				}
			}

			in.close();
			// close all file writters
			for (BufferedWriter out : mapOfFiles.values()) {
				out.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return listOfFiles;
	}

}
