package edu.scripps.yates.shared.model;

public enum FileFormat {

	EXCEL("Excel (xls,xlsx)", false, false), CENSUS_CHRO_XML("Census chro output (census_chro.xml)", true,
			true), CENSUS_OUT_TXT("Census output (census-out.txt)", true, true), DTA_SELECT_FILTER_TXT(
					"DTASelect output (DTASelect-filter.txt)", true, false), FASTA("Fasta file", false, false);
	private final String name;
	private final boolean dataFile;
	private final boolean quantitativeData;

	private FileFormat(String name, boolean dataFile, boolean quantitativeData) {
		this.name = name;
		this.dataFile = dataFile;
		this.quantitativeData = quantitativeData;
	}

	public static FileFormat getFileFormatFromString(String string) {
		final FileFormat[] values = values();
		for (FileFormat fileFormat : values) {
			if (fileFormat.name().equalsIgnoreCase(string))
				return fileFormat;
			if (fileFormat.getName().equalsIgnoreCase(string))
				return fileFormat;
		}
		return null;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @return the dataFile
	 */
	public boolean isDataFile() {
		return dataFile;
	}

	/**
	 * @return the quantitativeData
	 */
	public boolean isQuantitativeData() {
		return quantitativeData;
	}

}
