package edu.scripps.yates.census.analysis.wrappers;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * To read results data from "_outStats.xls" files
 * 
 * @author Salva
 * 
 */
public class OutStatsReader {

	private final File statisticsFile;
	private final List<OutStatsLine> lines = new ArrayList<OutStatsLine>();
	public static final Comparator<OutStatsLine> comparatorByFDR = new Comparator<OutStatsLine>() {
		@Override
		public int compare(OutStatsLine o1, OutStatsLine o2) {
			return Double.compare(o1.getFDR(), o2.getFDR());
		}
	};

	public OutStatsReader(File statisticsFile) throws IOException {
		if (statisticsFile == null || !statisticsFile.exists())
			throw new IllegalArgumentException(
					"stats output file doesn't exist");
		this.statisticsFile = statisticsFile;
		process();
	}

	private void process() throws IOException {
		InputStream fis = null;
		BufferedReader br = null;
		String line = null;
		try {
			fis = new FileInputStream(statisticsFile);
			br = new BufferedReader(new InputStreamReader(fis,
					Charset.forName("UTF-8")));
			boolean firstLine = true;
			while ((line = br.readLine()) != null) {
				// skip first line: header
				if (firstLine) {
					firstLine = false;
					continue;
				}
				final OutStatsLine outStatsLine = new OutStatsLine(line);
				lines.add(outStatsLine);
			}
		} finally {
			// Done with the file
			if (br != null)
				br.close();
			br = null;
			fis = null;
		}

	}

	@Override
	public String toString() {
		return super.toString();
	}

	/**
	 * Gets the data sorted by a given {@link Comparator}
	 * 
	 * @param comparator
	 * @return
	 */
	public List<OutStatsLine> getData(Comparator<OutStatsLine> comparator) {
		Collections.sort(lines, comparator);
		return lines;
	}

	/**
	 * Gets the data by default sorted by FDR
	 * 
	 * @param comparator
	 * @return
	 */
	public List<OutStatsLine> getData() {
		Collections.sort(lines, comparatorByFDR);
		return lines;
	}
}
