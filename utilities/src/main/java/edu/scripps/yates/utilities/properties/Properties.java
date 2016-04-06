package edu.scripps.yates.utilities.properties;

import java.io.IOException;
import java.io.InputStream;

public class Properties {
	private static final String PROPERTIES_FILE = "proteindb.properties";

	public static java.util.Properties getProperties() throws Exception {
		ClassLoader cl = Properties.class.getClassLoader();
		InputStream is;

		is = cl.getResourceAsStream(PROPERTIES_FILE);
		if (is == null)
			throw new Exception(PROPERTIES_FILE + " file not found");

		java.util.Properties prop = new java.util.Properties();
		try {
			prop.load(is);
		} catch (IOException e) {
			e.printStackTrace();
			throw new IllegalArgumentException(e);
		}
		return prop;
	}

	public static String getPropertyValue(String propertyName) {
		try {
			return getProperties().getProperty(propertyName);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}
