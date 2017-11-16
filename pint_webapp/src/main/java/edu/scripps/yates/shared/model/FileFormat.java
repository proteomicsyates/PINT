package edu.scripps.yates.shared.model;

import edu.scripps.yates.shared.exceptions.PintException.PINT_ERROR_TYPE;
import edu.scripps.yates.shared.exceptions.PintRuntimeException;

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
		if ("".equals(string)) {
			return null;
		}
		final FileFormat[] values = values();
		for (FileFormat fileFormat : values) {
			if (fileFormat.name().equalsIgnoreCase(string))
				return fileFormat;
			if (fileFormat.getName().equalsIgnoreCase(string))
				return fileFormat;
		}
		throw new PintRuntimeException(
				"The value '" + string + "' is not supported as a valid " + FileFormat.class.getCanonicalName(),
				PINT_ERROR_TYPE.VALUE_NOT_SUPPORTED);
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
