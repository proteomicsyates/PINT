package edu.scripps.yates.census.analysis.wrappers;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;

/**
 * To read results data from "_infoFile" files, like Variances or K constants
 * 
 * @author Salva
 * 
 */
public class InfoFileReader {
	private final File infoFile;

	public InfoFileReader(File infoFile) {
		this.infoFile = infoFile;
	}

	public Double getResultValue(SanXotResultProperty property)
			throws NumberFormatException, IOException {

		InputStream fis = null;
		BufferedReader br = null;
		String line = null;
		try {
			fis = new FileInputStream(infoFile);
			br = new BufferedReader(new InputStreamReader(fis,
					Charset.forName("UTF-8")));
			while ((line = br.readLine()) != null) {
				if (line.startsWith(property.getLineBegin())) {
					final int indexOf = line.indexOf(property.getLineBegin())
							+ property.getLineBegin().length();
					final String trim = line.substring(indexOf).trim();
					final Double value = Double.valueOf(trim);
					return value;
				}
			}
		} finally {
			// Done with the file
			if (br != null)
				br.close();
			br = null;
			fis = null;
		}
		return null;
	}
}
