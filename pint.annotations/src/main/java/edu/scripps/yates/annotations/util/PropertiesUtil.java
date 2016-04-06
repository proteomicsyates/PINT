package edu.scripps.yates.annotations.util;

import java.io.IOException;
import java.io.InputStream;

import org.apache.log4j.Logger;

public class PropertiesUtil {
	private static final Logger log = Logger.getLogger(PropertiesUtil.class);
	public static final String UNIPROT_PROPERTIES_FILE = "proteindb.uniprot.properties";
	public static final String MINIMUM_LENGHT_PROP = "minimum.length.text.comparison";
	public static final String UNIPROT_RELEASES_NOTES_PROP = "uniprot.releasenotes.url";
	public static final String UNIPROT_EBI_SERVER_PROP = "uniprot.ebi.server.url";
	public static final String UNIPROT_SERVER_PROP = "uniprot.server.url";

	public static final String OMIM_PROPERTIES_FILE = "omim.properties";
	public static final String OMIM_URL_PROP = "omim.url";
	public static final String OMIM_ENTRY_PROP = "omim.entry";

	public static final String UNIREF_PROPERTIES_FILE = "uniref.properties";
	public static final String UNIREF_URL_PROP = "uniref.url";

	// from http://www.uniprot.org/docs/ptmlist.txt
	public static final String UNIPROT_PTM_CV_LIST_FILE_PROP = "uniprot.ptm.cv.file";

	private static PropertiesUtil instance;
	private static java.util.Properties prop;
	public String currentPropertyFile;

	private PropertiesUtil(String propertyFilesName) {
		loadProperties(propertyFilesName);
	}

	public static PropertiesUtil getInstance(String propertyFilesName) {
		if (instance == null || !instance.currentPropertyFile.equals(propertyFilesName)) {
			instance = new PropertiesUtil(propertyFilesName);
		}
		return instance;
	}

	private void loadProperties(String propertyFileName) {
		ClassLoader cl = PropertiesUtil.class.getClassLoader();
		InputStream is;

		is = cl.getResourceAsStream(propertyFileName);
		if (is == null)
			throw new IllegalArgumentException(propertyFileName + " file not found");

		prop = new java.util.Properties();
		currentPropertyFile = propertyFileName;
		try {
			prop.load(is);
			log.debug("Properties file '" + propertyFileName + "'loaded correctly");
		} catch (IOException e) {
			e.printStackTrace();
			throw new IllegalArgumentException(e);
		}

	}

	public String getPropertyValue(String propertyName) {
		try {
			log.debug("Getting property '" + propertyName + "' from " + currentPropertyFile);
			final String property = prop.getProperty(propertyName);
			if (property == null) {
				log.warn("Property '" + propertyName + "' is not found in " + currentPropertyFile);
			} else {
				log.debug("Returning " + property);
				return property;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}
