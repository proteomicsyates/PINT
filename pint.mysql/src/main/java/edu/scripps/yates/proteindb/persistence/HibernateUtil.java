package edu.scripps.yates.proteindb.persistence;

import java.io.File;
import java.net.URL;
import java.util.Map;

import org.apache.log4j.Logger;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import edu.scripps.yates.utilities.properties.PropertiesUtil;

// Checks the SERVER_TEST env variable:
// if true: uses test database (using hibernate.cfg.test.xml)
// if false: uses production database (using hibernate.cfg.xml)
public class HibernateUtil {
	private final SessionFactory sessionFactory;
	private static Logger log = Logger.getLogger(HibernateUtil.class);
	private static HibernateUtil instance;
	private static boolean testEnabled;

	private HibernateUtil(File propertiesFile) {
		sessionFactory = buildSessionFactory(propertiesFile);
	}

	public static void initSessionFactory(boolean testEnabled, File propertiesFile) {
		HibernateUtil.getInstance(testEnabled, propertiesFile);
	}

	public static void initSessionFactory(File propertiesFile) {
		HibernateUtil.getInstance(propertiesFile);
	}

	/**
	 *
	 * @param testEnabled
	 *            indicate if the sessionFactory is going to point to the test
	 *            database or not
	 * @return a singleton instance of the class
	 */
	public static HibernateUtil getInstance(boolean testEnabled, File propertiesFile) {
		HibernateUtil.testEnabled = testEnabled;
		if (instance == null) {
			instance = new HibernateUtil(propertiesFile);
		}
		return instance;
	}

	public static HibernateUtil getInstance(File propertiesFile) {
		HibernateUtil.testEnabled = false;
		if (instance == null) {
			instance = new HibernateUtil(propertiesFile);
		}
		return instance;
	}

	private SessionFactory buildSessionFactory(File propertiesFile) {
		try {
			// Create the SessionFactory from hibernate.cfg.xml
			// new Configuration().configure(hibernate.cfg.test.xml)
			Map<String, String> env = System.getenv();

			log.info("Checking environment variable SERVER_TEST in HibernateUtil");
			log.info("Creating sessionFactory in HibernateUtil");
			Configuration configure = null;
			if (HibernateUtil.testEnabled
					|| (env.get("SERVER_TEST") != null && env.get("SERVER_TEST").equals("true"))) {
				if (HibernateUtil.testEnabled) {
					log.info("testEnabled = TRUE -> using hibernate.cfg.test.xml");
				} else {
					log.info("SERVER_TEST = TRUE -> using hibernate.cfg.test.xml");
				}
				// return new Configuration().configure(new
				// ClassPathResource("hibernate.cfg.test.xml").getFile()).buildSessionFactory();

				URL testCfgFileResource = getClass().getResource("/hibernate.cfg.test.xml");
				if (testCfgFileResource != null) {
					configure = new Configuration().configure(testCfgFileResource);
				} else {
					log.info("hibernate.cfg.test.xml not found. Trying with hibernate.cgf.xml");
				}
			}
			if (configure == null) {
				log.info("SERVER_TEST != TRUE -> using hibernate.cfg.xml");
				URL resource = getClass().getResource("/hibernate.cfg.xml");
				configure = new Configuration().configure(resource);
			}
			// overwrite url, username and password if defined in a
			// propertiesFile file by the user
			if (propertiesFile != null && propertiesFile.exists()) {
				try {
					final java.util.Properties properties = PropertiesUtil.getProperties(propertiesFile);
					final String dbUsername = properties.getProperty("db_username");
					if (dbUsername != null) {
						configure.setProperty("hibernate.connection.username", dbUsername);
					}
					final String dbURL = properties.getProperty("db_url");
					if (dbURL != null) {
						configure.setProperty("hibernate.connection.url", dbURL);
					}
					final String dbPassword = properties.getProperty("db_password");
					if (dbPassword != null) {
						configure.setProperty("hibernate.connection.password", dbPassword);
					}
				} catch (Exception e) {
					log.warn(e);
				}
			}
			return configure.buildSessionFactory();

			// ClassPathResource cpr = new
			// ClassPathResource("/hibernate.cfg.xml");
			// return new Configuration().configure(cpr.getFile())
			// .buildSessionFactory();

		} catch (Throwable ex) {
			// Make sure you log the exception, as it might be swallowed
			ex.printStackTrace();
			log.error("Initial SessionFactory creation failed." + ex.getMessage());

			throw new ExceptionInInitializerError(ex);
		}
	}

	public SessionFactory getSessionFactory() {
		return sessionFactory;
	}
}
