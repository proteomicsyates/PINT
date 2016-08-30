package edu.scripps.yates.server.configuration;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import edu.scripps.yates.dbindex.util.PropertiesReader;

public class ConfigurationPropertiesIO {

	private final static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(PropertiesReader.class);
	private static final String OMIM_PROPERTY = "omim.key";
	private static final String ADMIN_PASSWORD_PROPERTY = "admin.password";
	private static File setupPropertiesFile;

	public static PintConfigurationProperties readProperties(File setupPropertiesFile) {
		if (setupPropertiesFile == null) {
			throw new IllegalArgumentException("setup properties file not valid or null");
		}
		if (!setupPropertiesFile.exists()) {
			return new PintConfigurationProperties();
		}
		ConfigurationPropertiesIO.setupPropertiesFile = setupPropertiesFile;
		try {
			InputStream inputStream = new FileInputStream(setupPropertiesFile);
			PintConfigurationProperties prop = readProperties(inputStream);
			return prop;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			throw new IllegalArgumentException(e);
		}
	}

	public static PintConfigurationProperties readProperties() {
		return readProperties(setupPropertiesFile);
	}

	private static PintConfigurationProperties readProperties(InputStream inputStream) {

		if (inputStream == null) {
			throw new IllegalArgumentException("input stream is null");
		}
		try {
			Properties prop = new Properties();
			prop.load(inputStream);
			return readPintParametersFromProperties(prop);
		} catch (IOException e) {
			e.printStackTrace();
			throw new IllegalArgumentException(e);
		}

	}

	public static void writeOmimKey(File setupPropertiesFile, String omimKey) {
		final PintConfigurationProperties properties = readProperties(setupPropertiesFile);
		properties.setOmimKey(omimKey);
		writeProperties(setupPropertiesFile, properties);
	}

	public static void writeAdminPassword(File setupPropertiesFile, String adminPassword) {
		final PintConfigurationProperties properties = readProperties(setupPropertiesFile);
		properties.setAdminPassword(adminPassword);
		writeProperties(setupPropertiesFile, properties);
	}

	private static void writeProperties(File setupPropertiesFile, PintConfigurationProperties properties) {
		FileWriter fw = null;
		try {
			fw = new FileWriter(setupPropertiesFile);
			if (properties.getOmimKey() != null) {
				fw.append(OMIM_PROPERTY).append("=").append(properties.getOmimKey()).append("\n");
			}
			if (properties.getAdminPassword() != null) {
				fw.append(ADMIN_PASSWORD_PROPERTY).append("=").append(properties.getAdminPassword()).append("\n");
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (fw != null) {
				try {
					fw.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	private static PintConfigurationProperties readPintParametersFromProperties(Properties prop) {
		PintConfigurationProperties ret = new PintConfigurationProperties();

		String property = prop.getProperty(OMIM_PROPERTY);
		if (property != null) {
			property = property.trim();
		}
		ret.setOmimKey(property);
		String property2 = prop.getProperty(ADMIN_PASSWORD_PROPERTY);
		if (property2 != null) {
			property2 = property2.trim();
		}
		ret.setAdminPassword(property2);

		return ret;
	}

}
