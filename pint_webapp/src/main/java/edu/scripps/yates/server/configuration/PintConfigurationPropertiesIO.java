package edu.scripps.yates.server.configuration;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.Set;

import edu.scripps.yates.annotations.omim.OmimException;
import edu.scripps.yates.annotations.omim.OmimRetriever;
import edu.scripps.yates.dbindex.util.PropertiesReader;
import edu.scripps.yates.proteindb.persistence.ContextualSessionHandler;
import edu.scripps.yates.server.tasks.RemoteServicesTasks;
import edu.scripps.yates.server.util.FileManager;
import edu.scripps.yates.shared.configuration.PintConfigurationProperties;
import edu.scripps.yates.shared.model.ProjectBean;
import edu.scripps.yates.shared.util.CryptoUtil;

public class PintConfigurationPropertiesIO {

	private final static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(PropertiesReader.class);
	private static final String OMIM_PROPERTY = "omim.key";
	private static final String ADMIN_PASSWORD_PROPERTY = "admin.password";
	private static final String DB_USER_NAME = "db_username";
	private static final String DB_PASSWORD = "db_password";
	private static final String DB_URL = "db_url";
	private static final String PROJECT_FILES_PATH = "projectFilesPath";
	private static final String PRELOAD_PUBLIC_PROJECTS = "preLoadPublicProjects";
	private static final String PROJECTS_TO_PRELOAD = "projectsToPreLoad";
	private static final String PROJECTS_TO_NOT_PRELOAD = "projectsToNotPreLoad";

	public static PintConfigurationProperties readProperties(File setupPropertiesFile) {
		if (setupPropertiesFile == null || !setupPropertiesFile.exists()) {
			return new PintConfigurationProperties();
		}
		try {
			InputStream inputStream = new FileInputStream(setupPropertiesFile);
			PintConfigurationProperties prop = readProperties(inputStream);
			return prop;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			throw new IllegalArgumentException(e);
		}
	}

	public static PintConfigurationProperties readProperties(InputStream inputStream) {

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

	public static void writeOmimKey(String omimKey, File setupPropertiesFile) {
		final PintConfigurationProperties properties = readProperties(setupPropertiesFile);
		properties.setOmimKey(omimKey);
		writeProperties(properties, setupPropertiesFile);
		if (omimKey != null && !"".equals(omimKey)) {
			try {
				checkOMIMKey(omimKey);
			} catch (OmimException e) {
				throw new IllegalArgumentException(e);
			}
		}
	}

	private static void checkOMIMKey(String omimKey) throws OmimException {

		OmimRetriever.checkOMIMKey(omimKey);
	}

	public static void writeAdminPassword(String adminPassword, File setupPropertiesFile) {
		if (adminPassword == null || "".equals(adminPassword)) {
			throw new IllegalArgumentException("PINT admin password cannot be empy");
		}
		final PintConfigurationProperties properties = readProperties(setupPropertiesFile);
		properties.setAdminPassword(adminPassword);
		writeProperties(properties, setupPropertiesFile);
	}

	private static void writeProperties(PintConfigurationProperties properties, File setupPropertiesFile) {
		FileWriter fw = null;
		try {
			fw = new FileWriter(setupPropertiesFile);
			if (properties.getOmimKey() != null) {
				fw.append(OMIM_PROPERTY).append("=").append(properties.getOmimKey()).append("\n");
			}
			if (properties.getAdminPassword() != null) {
				fw.append(ADMIN_PASSWORD_PROPERTY).append("=").append(properties.getAdminPassword()).append("\n");
			}
			if (properties.getDb_password() != null) {
				fw.append(DB_PASSWORD).append("=").append(properties.getDb_password()).append("\n");
			}
			if (properties.getDb_url() != null) {
				fw.append(DB_URL).append("=").append(properties.getDb_url()).append("\n");
			}
			if (properties.getDb_username() != null) {
				fw.append(DB_USER_NAME).append("=").append(properties.getDb_username()).append("\n");
			}
			if (properties.getOmimKey() != null) {
				fw.append(OMIM_PROPERTY).append("=").append(properties.getOmimKey()).append("\n");
			}
			if (properties.getProjectFilesPath() != null) {
				fw.append(PROJECT_FILES_PATH).append("=").append(properties.getProjectFilesPath()).append("\n");
			}
			if (properties.isPreLoadPublicProjects() != null) {
				fw.append(PRELOAD_PUBLIC_PROJECTS).append("=").append(properties.isPreLoadPublicProjects().toString())
						.append("\n");
			}
			if (properties.getProjectsToPreLoad() != null) {
				fw.append(PROJECTS_TO_PRELOAD).append("=").append(properties.getProjectsToPreLoad()).append("\n");

			}
			if (properties.getProjectsToNotPreLoad() != null) {
				fw.append(PROJECTS_TO_NOT_PRELOAD).append("=").append(properties.getProjectsToNotPreLoad())
						.append("\n");
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
		property = prop.getProperty(ADMIN_PASSWORD_PROPERTY);
		if (property != null) {
			property = property.trim();
		}
		ret.setAdminPassword(property);
		property = prop.getProperty(DB_PASSWORD);
		if (property != null) {
			property = property.trim();
		}
		ret.setDb_password(property);
		property = prop.getProperty(DB_URL);
		if (property != null) {
			property = property.trim();
		}
		ret.setDb_url(property);
		property = prop.getProperty(DB_USER_NAME);
		if (property != null) {
			property = property.trim();
		}
		ret.setDb_username(property);

		property = prop.getProperty(PRELOAD_PUBLIC_PROJECTS);
		if (property != null) {
			property = property.trim();
		}
		ret.setPreLoadPublicProjects(Boolean.valueOf(property));
		property = prop.getProperty(PROJECT_FILES_PATH);
		if (property != null) {
			property = property.trim();
		}
		ret.setProjectFilesPath(property);
		property = prop.getProperty(PROJECTS_TO_NOT_PRELOAD);
		if (property != null) {
			property = property.trim();
		}
		ret.setProjectsToNotPreLoad(property);
		property = prop.getProperty(PROJECTS_TO_PRELOAD);
		if (property != null) {
			property = property.trim();
		}
		ret.setProjectsToPreLoad(property);
		return ret;
	}

	public static void writeDBPassword(String dbPassword, File setupPropertiesFile) {
		if (dbPassword == null || "".equals(dbPassword)) {
			throw new IllegalArgumentException("PINT database connection password cannot be empy");
		}
		final PintConfigurationProperties properties = readProperties(setupPropertiesFile);
		String decryptedPassword = CryptoUtil.decrypt(dbPassword);
		properties.setDb_password(decryptedPassword);
		writeProperties(properties, setupPropertiesFile);
		testDatabaseConnection(setupPropertiesFile);
	}

	public static void writeDBUserName(String dbUserName, File setupPropertiesFile) {
		if (dbUserName == null || "".equals(dbUserName)) {
			throw new IllegalArgumentException("PINT database connection user name cannot be empty");
		}
		final PintConfigurationProperties properties = readProperties(setupPropertiesFile);
		properties.setDb_username(dbUserName);
		writeProperties(properties, setupPropertiesFile);
		testDatabaseConnection(setupPropertiesFile);
	}

	private static void testDatabaseConnection(File setupPropertiesFile) {
		ContextualSessionHandler.clearSessionFactory();
		ContextualSessionHandler.getSessionFactory(setupPropertiesFile);

		ContextualSessionHandler.openSession();
		ContextualSessionHandler.beginGoodTransaction();
		ContextualSessionHandler.finishGoodTransaction();
		ContextualSessionHandler.closeSession();
	}

	public static void writeDBURL(String dbURL, File setupPropertiesFile) {
		if (dbURL == null || "".equals(dbURL)) {
			throw new IllegalArgumentException(
					"Database connection URL cannot be empty. Try something like: 'jdbc:mysql://your_server:3306/database_name'");
		}
		final PintConfigurationProperties properties = readProperties(setupPropertiesFile);
		properties.setDb_url(dbURL);
		writeProperties(properties, setupPropertiesFile);
		testDatabaseConnection(setupPropertiesFile);

	}

	public static void writeProjectFilesPath(String projectFilesPath, File setupPropertiesFile) {
		// replace \ by /
		projectFilesPath = projectFilesPath.replace("\\", "/");
		// replace // by /
		projectFilesPath = projectFilesPath.replace("//", "/");
		final PintConfigurationProperties properties = readProperties(setupPropertiesFile);
		properties.setProjectFilesPath(projectFilesPath);
		writeProperties(properties, setupPropertiesFile);
		FileManager.resetProjectfilePath();
	}

	public static void writeProjectsToPreload(String projectsToPreload, File setupPropertiesFile) {
		checkCVList(projectsToPreload, setupPropertiesFile);
		final PintConfigurationProperties properties = readProperties(setupPropertiesFile);
		properties.setProjectsToPreLoad(projectsToPreload);
		writeProperties(properties, setupPropertiesFile);
	}

	private static void checkCVList(String projectsToPreload, File setupPropertiesFile) {
		if (projectsToPreload != null && !"".equals(projectsToPreload)) {
			if (projectsToPreload.contains(",")) {
				String[] split = projectsToPreload.split(",");
				for (String projectTag : split) {
					checkProjectExistence(projectTag.trim(), setupPropertiesFile);
				}
			} else {
				String projectTag = projectsToPreload.trim();
				checkProjectExistence(projectTag, setupPropertiesFile);
			}
		}
	}

	private static void checkProjectExistence(String projectTag, File setupPropertiesFile) {
		testDatabaseConnection(setupPropertiesFile);
		ContextualSessionHandler.openSession();
		ContextualSessionHandler.beginGoodTransaction();
		Set<ProjectBean> projectBeans = RemoteServicesTasks.getProjectBeans();
		StringBuilder sb = new StringBuilder();
		for (ProjectBean projectBean : projectBeans) {
			if (!"".equals(sb.toString())) {
				sb.append(", ");
			}
			sb.append(projectBean.getTag());
			if (projectBean.getTag().equals(projectTag)) {
				return;
			}
		}
		throw new IllegalArgumentException(
				"'" + projectTag + "' not found in the database. Available projects are: '" + sb.toString() + "'");
	}

	public static void writeProjectsToNotPreload(String projectsToNotPreload, File setupPropertiesFile) {
		checkCVList(projectsToNotPreload, setupPropertiesFile);
		final PintConfigurationProperties properties = readProperties(setupPropertiesFile);
		properties.setProjectsToNotPreLoad(projectsToNotPreload);
		writeProperties(properties, setupPropertiesFile);
	}

	public static void writePreLoadPublicProjects(boolean preLoadPublicProjects, File setupPropertiesFile) {
		final PintConfigurationProperties properties = readProperties(setupPropertiesFile);
		properties.setPreLoadPublicProjects(preLoadPublicProjects);
		writeProperties(properties, setupPropertiesFile);
	}

}
