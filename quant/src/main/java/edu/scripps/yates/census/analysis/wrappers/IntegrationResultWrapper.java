package edu.scripps.yates.census.analysis.wrappers;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;

import edu.scripps.yates.census.analysis.FileMappingResults;

public class IntegrationResultWrapper {
	private static final Logger log = Logger.getLogger(IntegrationResultWrapper.class);
	private final File workingFolder;
	private final String prefix;
	private String higherLevelSuffix;
	private String infoFileSuffix;
	private String lowerLevelVSuffix;
	private String lowerLevelWSuffix;
	private String statisticsSuffix;
	private String graphSuffix;
	private InfoFileReader infoFileReader;
	private final int lowLevel, upperLevel;
	private OutlierRemovalResultWrapper outlierRemovalResult;
	private final FileMappingResults fileMappingResults;
	private OutStatsReader outStatsReader;
	private final static String DEFAULT_HIGHERLEVEL_SUFFIX = "_higherLevel";
	private final static String DEFAULT_INFO_FILE_SUFFIX = "_infoFile";
	private final static String DEFAULT_LOWERLEVEL_V_SUFFIX = "_lowerNormV";
	private final static String DEFAULT_LOWERLEVEL_W_SUFFIX = "_lowerNormW";
	private final static String DEFAULT_STATISTICS_SUFFIX = "_outStats";
	private final static String DEFAULT_GRAPH_SUFFIX = "_outGraph";

	public IntegrationResultWrapper(File workingFolder, String prefix, int lowLevel, int upperLevel,
			FileMappingResults fileMappingResults) {
		this.workingFolder = workingFolder;
		this.prefix = prefix;
		this.lowLevel = lowLevel;
		this.upperLevel = upperLevel;
		this.fileMappingResults = fileMappingResults;
		higherLevelSuffix = DEFAULT_HIGHERLEVEL_SUFFIX;
		infoFileSuffix = DEFAULT_INFO_FILE_SUFFIX;
		lowerLevelVSuffix = DEFAULT_LOWERLEVEL_V_SUFFIX;
		lowerLevelWSuffix = DEFAULT_LOWERLEVEL_W_SUFFIX;
		statisticsSuffix = DEFAULT_STATISTICS_SUFFIX;
		graphSuffix = DEFAULT_GRAPH_SUFFIX;
	}

	public IntegrationResultWrapper(File workingFolder, String prefix, int lowLevel, int upperLevel) {
		this.workingFolder = workingFolder;
		this.prefix = prefix;
		this.lowLevel = lowLevel;
		this.upperLevel = upperLevel;
		fileMappingResults = null;
		higherLevelSuffix = DEFAULT_HIGHERLEVEL_SUFFIX;
		infoFileSuffix = DEFAULT_INFO_FILE_SUFFIX;
		lowerLevelVSuffix = DEFAULT_LOWERLEVEL_V_SUFFIX;
		lowerLevelWSuffix = DEFAULT_LOWERLEVEL_W_SUFFIX;
		statisticsSuffix = DEFAULT_STATISTICS_SUFFIX;
		graphSuffix = DEFAULT_GRAPH_SUFFIX;
	}

	/**
	 * @return the outlierRemovalResult
	 */
	public OutlierRemovalResultWrapper getOutlierRemovalResult() {
		return outlierRemovalResult;
	}

	/**
	 * @param outlierRemovalResult
	 *            the outlierRemovalResult to set
	 */
	public void setOutlierRemovalResult(OutlierRemovalResultWrapper outlierRemovalResult) {
		this.outlierRemovalResult = outlierRemovalResult;
	}

	/**
	 * @return the lowLevel
	 */
	public int getLowLevel() {
		return lowLevel;
	}

	/**
	 * @return the upperLevel
	 */
	public int getUpperLevel() {
		return upperLevel;
	}

	public File getHigherLevelDataFile() {
		File higherLevelFile = new File(
				workingFolder.getAbsolutePath() + File.separator + prefix + higherLevelSuffix + ".xls");
		if (higherLevelFile.exists() && higherLevelFile.isFile())
			return higherLevelFile;
		return null;
	}

	/**
	 * Gets the content of the file higherLevel in the integration
	 *
	 * @return
	 */
	public Map<String, SanxotQuantResult> getHighLevelRatios() {
		return getRatiosAndWeightsFromFile(getHigherLevelDataFile());
	}

	/**
	 * Gets the content of the file outStats in the integration
	 *
	 * @return
	 */
	public Map<String, SanxotQuantResult> getOutStatsRatios() {
		return getRatiosAndWeightsFromFile(getStatisticsFile());
	}

	/**
	 * Gets the content of the file lowerW in the integration
	 *
	 * @return
	 */
	public Map<String, SanxotQuantResult> getLowerLevelRatiosAndNormalizedWeigths() {
		return getRatiosAndWeightsFromFile(getLowerLevelWFile());
	}

	/**
	 * Gets the content of the file lowerV in the integration
	 *
	 * @return
	 */
	public Map<String, SanxotQuantResult> getLowerLevelRatiosAndWeigths() {
		return getRatiosAndWeightsFromFile(getLowerLevelVFile());
	}

	/**
	 * Read the {@link SanxotQuantResult} from the file. The file should be the
	 * one that have '_outStats' as suffix, which is a TAB separated value file
	 * and have:
	 * <ul>
	 * <li>the key of the quant (protein ACC, peptide seq, protein cluster
	 * key...) in the 4th column</li>
	 * <li>the log2ratio in the 5th column</li>
	 * <li>the weight (Vinf) in the 6th column</li>
	 * <li>the Z value in the 8th column</li>
	 * <li>the FDR value in the 9th column</li>
	 * </ul>
	 *
	 * @param sanxotQuantResultsByKey
	 * @param file
	 */
	private Map<String, SanxotQuantResult> getSanXotQuantResultFromFile(File file) {
		Map<String, SanxotQuantResult> ret = new HashMap<String, SanxotQuantResult>();
		InputStream fis;
		BufferedReader br = null;
		String line;

		try {
			fis = new FileInputStream(file);
			br = new BufferedReader(new InputStreamReader(fis, Charset.forName("UTF-8")));
			boolean firsLine = true;
			while ((line = br.readLine()) != null) {
				if (firsLine) {
					firsLine = false;
					continue;
				}

				SanxotQuantResult ratioAndWeight = new SanxotQuantResult(line);
				ret.put(ratioAndWeight.getKey(), ratioAndWeight);

			}

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			// Done with the file
			try {
				br.close();
			} catch (IOException e) {

			}
			br = null;
			fis = null;
		}
		return ret;
	}

	public static Map<String, SanxotQuantResult> getSanXotQuantResultFromDataFile(File file) {
		Map<String, SanxotQuantResult> ret = new HashMap<String, SanxotQuantResult>();
		InputStream fis;
		BufferedReader br = null;
		String line;

		try {
			fis = new FileInputStream(file);
			br = new BufferedReader(new InputStreamReader(fis, Charset.forName("UTF-8")));
			while ((line = br.readLine()) != null) {
				if (line.trim().startsWith("#") || line.trim().startsWith("id")) {
					continue;
				}
				if (line.contains("\t")) {
					final String[] split = line.split("\t");
					if (split.length == 3) {
						try {
							SanxotQuantResult ratioAndWeight = new SanxotQuantResult(split[0], Double.valueOf(split[1]),
									Double.valueOf(split[2]));
							ret.put(ratioAndWeight.getKey(), ratioAndWeight);
						} catch (NumberFormatException e) {
							log.warn(e);
						}
					}
				}
			}

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			// Done with the file
			try {
				br.close();
			} catch (IOException e) {

			}
			br = null;
			fis = null;
		}
		return ret;
	}

	public static Map<String, Set<String>> getRelationShipsFromRelatFile(File file) {
		Map<String, Set<String>> ret = new HashMap<String, Set<String>>();
		InputStream fis;
		BufferedReader br = null;
		String line;

		try {
			fis = new FileInputStream(file);
			br = new BufferedReader(new InputStreamReader(fis, Charset.forName("UTF-8")));
			while ((line = br.readLine()) != null) {
				if (line.trim().startsWith("#")) {
					continue;
				}
				if (line.contains("\t")) {
					final String[] split = line.split("\t");
					if (split.length == 2) {
						try {
							String upperLevel = split[0];
							String lowerLevel = split[1];
							if (ret.containsKey(upperLevel)) {
								ret.get(upperLevel).add(lowerLevel);
							} else {
								Set<String> set = new HashSet<String>();
								set.add(lowerLevel);
								ret.put(upperLevel, set);
							}
						} catch (NumberFormatException e) {
							log.warn(e);
						}
					}
				}
			}

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			// Done with the file
			try {
				br.close();
			} catch (IOException e) {

			}
			br = null;
			fis = null;
		}
		return ret;
	}

	private Map<String, SanxotQuantResult> getRatiosAndWeightsFromFile(File file) {
		Map<String, SanxotQuantResult> ret = new HashMap<String, SanxotQuantResult>();
		InputStream fis;
		BufferedReader br = null;
		String line;

		try {
			fis = new FileInputStream(file);
			br = new BufferedReader(new InputStreamReader(fis, Charset.forName("UTF-8")));
			boolean firsLine = true;
			while ((line = br.readLine()) != null) {
				if (firsLine) {
					firsLine = false;
					continue;
				}

				SanxotQuantResult ratioAndWeight = new SanxotQuantResult(line);
				ret.put(ratioAndWeight.getKey(), ratioAndWeight);

			}

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			// Done with the file
			try {
				br.close();
			} catch (IOException e) {

			}
			br = null;
			fis = null;
		}
		return ret;
	}

	public File getLowerLevelVFile() {
		File ret = new File(workingFolder.getAbsolutePath() + File.separator + prefix + lowerLevelVSuffix + ".xls");
		if (ret.exists() && ret.isFile())
			return ret;
		return null;
	}

	public File getLowerLevelWFile() {
		File ret = new File(workingFolder.getAbsolutePath() + File.separator + prefix + lowerLevelWSuffix + ".xls");
		if (ret.exists() && ret.isFile())
			return ret;
		return null;
	}

	public File getStatisticsFile() {
		File ret = new File(workingFolder.getAbsolutePath() + File.separator + prefix + statisticsSuffix + ".xls");
		if (ret.exists() && ret.isFile())
			return ret;
		return null;
	}

	public File getGraphDataFile() {
		File ret = new File(workingFolder.getAbsolutePath() + File.separator + prefix + graphSuffix + ".png");
		if (ret.exists() && ret.isFile())
			return ret;
		return null;
	}

	public File getInfoFile() {
		File calibratedFile = new File(
				workingFolder.getAbsolutePath() + File.separator + prefix + infoFileSuffix + ".txt");
		if (calibratedFile.exists() && calibratedFile.isFile())
			return calibratedFile;
		return null;
	}

	public Double getIntegrationVariance() throws IOException {
		return getInfoFileReader().getResultValue(SanXotResultProperty.VARIANCE);
	}

	public List<OutStatsLine> getResultData() {
		try {
			if (outStatsReader == null) {
				outStatsReader = new OutStatsReader(getStatisticsFile());
			}
			return outStatsReader.getData();
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

	private InfoFileReader getInfoFileReader() {
		if (infoFileReader == null)
			infoFileReader = new InfoFileReader(getInfoFile());
		return infoFileReader;
	}

	/**
	 * @return the workingFolder
	 */
	public File getWorkingFolder() {
		return workingFolder;
	}

	/**
	 * @return the higherLevelSuffix
	 */
	public String getIntegrationSuffix() {
		return higherLevelSuffix;
	}

	/**
	 * @param higherLevelSuffix
	 *            the higherLevelSuffix to set
	 */
	public void setHigherLevelSuffix(String higherLevelSuffix) {
		this.higherLevelSuffix = higherLevelSuffix;
	}

	/**
	 * @return the infoFileSuffix
	 */
	public String getInfoFileSuffix() {
		return infoFileSuffix;
	}

	/**
	 * @param infoFileSuffix
	 *            the infoFileSuffix to set
	 */
	public void setInfoFileSuffix(String infoFileSuffix) {
		this.infoFileSuffix = infoFileSuffix;
	}

	/**
	 * @return the prefix
	 */
	public String getPrefix() {
		return prefix;
	}

	/**
	 * @return the lowerLevelVSuffix
	 */
	public String getLowerLevelVSuffix() {
		return lowerLevelVSuffix;
	}

	/**
	 * @param lowerLevelVSuffix
	 *            the lowerLevelVSuffix to set
	 */
	public void setLowerLevelVSuffix(String lowerLevelVSuffix) {
		this.lowerLevelVSuffix = lowerLevelVSuffix;
	}

	/**
	 * @return the lowerLevelWSuffix
	 */
	public String getLowerLevelWSuffix() {
		return lowerLevelWSuffix;
	}

	/**
	 * @param lowerLevelWSuffix
	 *            the lowerLevelWSuffix to set
	 */
	public void setLowerLevelWSuffix(String lowerLevelWSuffix) {
		this.lowerLevelWSuffix = lowerLevelWSuffix;
	}

	/**
	 * @return the statisticsSuffix
	 */
	public String getStatisticsSuffix() {
		return statisticsSuffix;
	}

	/**
	 * @param statisticsSuffix
	 *            the statisticsSuffix to set
	 */
	public void setStatisticsSuffix(String statisticsSuffix) {
		this.statisticsSuffix = statisticsSuffix;
	}

	/**
	 * @return the graphSuffix
	 */
	public String getGraphSuffix() {
		return graphSuffix;
	}

	/**
	 * @param graphSuffix
	 *            the graphSuffix to set
	 */
	public void setGraphSuffix(String graphSuffix) {
		this.graphSuffix = graphSuffix;
	}

	/**
	 * @return the higherLevelSuffix
	 */
	public String getHigherLevelSuffix() {
		return higherLevelSuffix;
	}

	@Override
	public String toString() {

		String ret = "Integration results:\n\tFrom level " + getLowLevel() + " to level " + getUpperLevel() + "\n";
		if (fileMappingResults != null) {
			ret += "\tFrom level " + fileMappingResults.getFileNameByLevel(getLowLevel()) + " to level "
					+ fileMappingResults.getFileNameByLevel(getUpperLevel()) + "\n";
		}
		try {
			ret += "\t\tVariance at this level=" + getIntegrationVariance() + "\n";
		} catch (IOException e) {
			e.printStackTrace();
			ret += "\t\tVariance at this level=" + e.getMessage() + "\n";
		}
		return ret;
	}
}
