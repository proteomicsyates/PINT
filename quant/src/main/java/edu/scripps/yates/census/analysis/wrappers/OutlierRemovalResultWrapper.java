package edu.scripps.yates.census.analysis.wrappers;

import java.io.File;

public class OutlierRemovalResultWrapper {
	private final File workingFolder;
	private final String prefix;
	private String infoFileSuffix;
	private String relatSuffix;
	private String outliersSuffix;
	private InfoFileReader infoFileReader;

	private final static String DEFAULT_OUTLIER_REMOVAL_SUFFIX = "_without_outliers";
	private final static String DEFAULT_INFO_FILE_SUFFIX = "_infoFile";
	private final static String DEFAULT_RELAT_SUFFIX = "_cleaned";
	private final static String DEFAULT_OUTLIERS_SUFFIX = "_outliers";
	public static final String DEFAULT_OUTLIER_REMOVAL_PREFIX = "outliers_removed_";

	public OutlierRemovalResultWrapper(File workingFolder, String prefix) {
		this.workingFolder = workingFolder;
		this.prefix = prefix;
		infoFileSuffix = DEFAULT_INFO_FILE_SUFFIX;
		relatSuffix = DEFAULT_RELAT_SUFFIX;
		outliersSuffix = DEFAULT_OUTLIERS_SUFFIX;

	}

	public File getOutliersFile() {
		File calibratedFile = new File(workingFolder.getAbsolutePath()
				+ File.separator + prefix + outliersSuffix + ".xls");
		if (calibratedFile.exists() && calibratedFile.isFile())
			return calibratedFile;
		return null;
	}

	public File getRelatFile() {
		File ret = new File(workingFolder.getAbsolutePath() + File.separator
				+ prefix + relatSuffix + ".xls");
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
	 * @return the relatSuffix
	 */
	public String getRelatSuffix() {
		return relatSuffix;
	}

	/**
	 * @param relatSuffix
	 *            the relatSuffix to set
	 */
	public void setRelatSuffix(String relatSuffix) {
		this.relatSuffix = relatSuffix;
	}

	/**
	 * @return the outliersSuffix
	 */
	public String getOutliersSuffix() {
		return outliersSuffix;
	}

	/**
	 * @param outliersSuffix
	 *            the outliersSuffix to set
	 */
	public void setOutliersSuffix(String outliersSuffix) {
		this.outliersSuffix = outliersSuffix;
	}

	/**
	 * @return the prefix
	 */
	public String getPrefix() {
		return prefix;
	}

	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "OutlierRemovalResultWrapper []\n";
	}

}
