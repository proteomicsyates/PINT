package edu.scripps.yates.proteindb.persistence;

import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Map;

import org.apache.log4j.Logger;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

// Checks the SERVER_TEST env variable:
// if true: uses test database (using hibernate.cfg.test.xml)
// if false: uses production database (using hibernate.cfg.xml)
public class HibernateUtil {
	private final SessionFactory sessionFactory;
	private static Logger log = Logger.getLogger(HibernateUtil.class);
	private static HibernateUtil instance;
	private static boolean testEnabled;
	private static String errorMessage;

	private HibernateUtil(String dbUsername, String dbPassword, String dbURL) {
		sessionFactory = buildSessionFactory(dbUsername, dbPassword, dbURL);
	}

	public static void initSessionFactory(boolean testEnabled, String dbUsername, String dbPassword, String dbURL) {
		HibernateUtil.getInstance(testEnabled, dbUsername, dbPassword, dbURL);
	}

	public static void initSessionFactory(String dbUsername, String dbPassword, String dbURL) {
		HibernateUtil.getInstance(dbUsername, dbPassword, dbURL);
	}

	public static void clearSessionFactory() {
		instance = null;
	}

	/**
	 *
	 * @param testEnabled
	 *            indicate if the sessionFactory is going to point to the test
	 *            database or not
	 * @return a singleton instance of the class
	 */
	public static HibernateUtil getInstance(boolean testEnabled, String dbUsername, String dbPassword, String dbURL) {
		HibernateUtil.testEnabled = testEnabled;
		if (instance == null) {
			instance = new HibernateUtil(dbUsername, dbPassword, dbURL);
		}
		return instance;
	}

	public static HibernateUtil getInstance(String dbUsername, String dbPassword, String dbURL) {
		HibernateUtil.testEnabled = false;
		if (instance == null) {
			instance = new HibernateUtil(dbUsername, dbPassword, dbURL);
		}
		return instance;
	}

	private SessionFactory buildSessionFactory(String dbUsername, String dbPassword, String dbURL) {
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

			if (dbUsername != null && !"".contentEquals(dbUsername)) {
				configure.setProperty("hibernate.connection.username", dbUsername);
			} else {
				throw new IllegalArgumentException("User name is required to connect to the database");
			}

			if (dbURL != null && !"".equals(dbURL)) {
				configure.setProperty("hibernate.connection.url", dbURL);
			} else {
				throw new IllegalArgumentException(
						"An URL where the database is accessible is required to connect to the database");
			}

			if (dbPassword != null && !"".contentEquals(dbPassword)) {
				configure.setProperty("hibernate.connection.password", dbPassword);
			} else {
				throw new IllegalArgumentException("Password is required to connect to the database");
			}
			// check connectino first
			checkDBConnection(dbURL, dbUsername, dbPassword);
			SessionFactory buildSessionFactory = configure.buildSessionFactory();
			errorMessage = null;
			return buildSessionFactory;

			// ClassPathResource cpr = new
			// ClassPathResource("/hibernate.cfg.xml");
			// return new Configuration().configure(cpr.getFile())
			// .buildSessionFactory();

		} catch (Throwable ex) {
			errorMessage = ex.getMessage();
			// Make sure you log the exception, as it might be swallowed
			ex.printStackTrace();
			log.error("Initial SessionFactory creation failed." + ex.getMessage());

			throw new IllegalArgumentException(ex);
		}
	}

	private void checkDBConnection(String dbURL, String dbUsername, String dbPassword) throws SQLException {
		checkDriver();
		Connection connection = DriverManager.getConnection(dbURL, dbUsername, dbPassword);
		log.info("Database connected using " + dbURL + " " + dbUsername + " " + dbPassword);
		connection.close();
	}

	private void checkDriver() {
		log.info("Loading driver...");

		try {
			Class.forName("com.mysql.jdbc.Driver");
			log.info("Driver loaded!");
		} catch (ClassNotFoundException e) {
			throw new IllegalStateException("Cannot find the driver in the classpath!", e);
		}
	}

	public SessionFactory getSessionFactory() {
		return sessionFactory;
	}
}
