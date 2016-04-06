package edu.scripps.yates.census.analysis.wrappers;

import java.io.File;
import java.io.IOException;

public class KalibrateResultWrapper {
	private final File workingFolder;
	private final String prefix;
	private String calibratedSuffix;
	private String infoFileSuffix;
	private String vRankGraphSuffix;
	private String vGraphSuffix;
	private String dataFileForGraphSuffix;
	private InfoFileReader infoFileReader;

	public final static String DEFAULT_CALIBRATED_PREFIX = "calibration_";
	private final static String DEFAULT_CALIBRATED_SUFFIX = "_calibrated";
	private final static String DEFAULT_INFO_FILE_SUFFIX = "_infoFile";
	private final static String DEFAULT_V_RANK_GRAPH_SUFFIX = "_outGraph_VRank";
	private final static String DEFAULT_V_GRAPH_SUFFIX = "_outGraph_VValue";
	private final static String DEFAULT_DATA_FILE_FOR_GRAPH_SUFFIX = "_outGraph_Data";

	public KalibrateResultWrapper(File workingFolder, String prefix) {
		this.workingFolder = workingFolder;
		this.prefix = prefix;
		calibratedSuffix = DEFAULT_CALIBRATED_SUFFIX;
		infoFileSuffix = DEFAULT_INFO_FILE_SUFFIX;
		vRankGraphSuffix = DEFAULT_V_RANK_GRAPH_SUFFIX;
		vGraphSuffix = DEFAULT_V_GRAPH_SUFFIX;
		dataFileForGraphSuffix = DEFAULT_DATA_FILE_FOR_GRAPH_SUFFIX;

	}

	public File getCalibratedDataFile() {
		File calibratedFile = new File(workingFolder.getAbsolutePath()
				+ File.separator + prefix + calibratedSuffix + ".xls");
		if (calibratedFile.exists() && calibratedFile.isFile())
			return calibratedFile;
		return null;
	}

	public File getvRankGraphFile() {
		File ret = new File(workingFolder.getAbsolutePath() + File.separator
				+ prefix + vRankGraphSuffix + ".png");
		if (ret.exists() && ret.isFile())
			return ret;
		return null;
	}

	public File getvGraphFile() {
		File ret = new File(workingFolder.getAbsolutePath() + File.separator
				+ prefix + vGraphSuffix + ".png");
		if (ret.exists() && ret.isFile())
			return ret;
		return null;
	}

	public File getDataFileForGraphFile() {
		File ret = new File(workingFolder.getAbsolutePath() + File.separator
				+ prefix + dataFileForGraphSuffix + ".xls");
		if (ret.exists() && ret.isFile())
			return ret;
		return null;
	}

	public File getInfoFile() {
		File ret = new File(workingFolder.getAbsolutePath() + File.separator
				+ prefix + infoFileSuffix + ".txt");
		if (ret.exists() && ret.isFile())
			return ret;
		return null;
	}

	public Double getCalibrationKConstant() throws IOException {
		return getInfoFileReader().getResultValue(SanXotResultProperty.K);
	}

	public Double getCalibrationVariance() throws IOException {
		return getInfoFileReader()
				.getResultValue(SanXotResultProperty.VARIANCE);
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
	 * @return the kalibratedSuffix
	 */
	public String getKalibratedSuffix() {
		return calibratedSuffix;
	}

	/**
	 * @param kalibratedSuffix
	 *            the kalibratedSuffix to set
	 */
	public void setKalibratedSuffix(String kalibratedSuffix) {
		calibratedSuffix = kalibratedSuffix;
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
	 * @return the vRankGraphSuffix
	 */
	public String getvRankGraphSuffix() {
		return vRankGraphSuffix;
	}

	/**
	 * @param vRankGraphSuffix
	 *            the vRankGraphSuffix to set
	 */
	public void setvRankGraphSuffix(String vRankGraphSuffix) {
		this.vRankGraphSuffix = vRankGraphSuffix;
	}

	/**
	 * @return the vGraphSuffix
	 */
	public String getvGraphSuffix() {
		return vGraphSuffix;
	}

	/**
	 * @param vGraphSuffix
	 *            the vGraphSuffix to set
	 */
	public void setvGraphSuffix(String vGraphSuffix) {
		this.vGraphSuffix = vGraphSuffix;
	}

	/**
	 * @return the dataFileForGraphSuffix
	 */
	public String getDataFileForGraphSuffix() {
		return dataFileForGraphSuffix;
	}

	/**
	 * @param dataFileForGraphSuffix
	 *            the dataFileForGraphSuffix to set
	 */
	public void setDataFileForGraphSuffix(String dataFileForGraphSuffix) {
		this.dataFileForGraphSuffix = dataFileForGraphSuffix;
	}
}
