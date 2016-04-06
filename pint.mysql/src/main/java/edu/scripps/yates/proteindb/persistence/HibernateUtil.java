package edu.scripps.yates.proteindb.persistence;

import java.net.URL;
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

	private HibernateUtil() {
		sessionFactory = buildSessionFactory();
	}

	public static void initSessionFactory(boolean testEnabled) {
		HibernateUtil.getInstance(testEnabled);
	}

	public static void initSessionFactory() {
		HibernateUtil.getInstance();
	}

	/**
	 *
	 * @param testEnabled
	 *            indicate if the sessionFactory is going to point to the test
	 *            database or not
	 * @return a singleton instance of the class
	 */
	public static HibernateUtil getInstance(boolean testEnabled) {
		HibernateUtil.testEnabled = testEnabled;
		if (instance == null) {
			instance = new HibernateUtil();
		}
		return instance;
	}

	public static HibernateUtil getInstance() {
		HibernateUtil.testEnabled = false;
		if (instance == null) {
			instance = new HibernateUtil();
		}
		return instance;
	}

	private SessionFactory buildSessionFactory() {
		try {
			// Create the SessionFactory from hibernate.cfg.xml
			// new Configuration().configure(hibernate.cfg.test.xml)
			Map<String, String> env = System.getenv();

			log.info("Checking environment variable SERVER_TEST in HibernateUtil");
			log.info("Creating sessionFactory in HibernateUtil");
			if (HibernateUtil.testEnabled
					|| (env.get("SERVER_TEST") != null && env.get("SERVER_TEST").equals("true"))) {
				if (HibernateUtil.testEnabled)
					log.info("testEnabled = TRUE -> using hibernate.cfg.test.xml");
				else
					log.info("SERVER_TEST = TRUE -> using hibernate.cfg.test.xml");
				// return new Configuration().configure(new
				// ClassPathResource("hibernate.cfg.test.xml").getFile()).buildSessionFactory();

				URL testCfgFileResource = getClass().getResource("/hibernate.cfg.test.xml");
				if (testCfgFileResource != null)
					return new Configuration().configure(testCfgFileResource).buildSessionFactory();
				else
					log.info("hibernate.cfg.test.xml not found. Trying with hibernate.cgf.xml");
			}
			log.info("SERVER_TEST != TRUE -> using hibernate.cfg.xml");
			URL resource = getClass().getResource("/hibernate.cfg.xml");
			return new Configuration().configure(resource).buildSessionFactory();

			// ClassPathResource cpr = new
			// ClassPathResource("/hibernate.cfg.xml");
			// return new Configuration().configure(cpr.getFile())
			// .buildSessionFactory();

		} catch (Throwable ex) {
			// Make sure you log the exception, as it might be swallowed
			ex.printStackTrace();
			System.err.println("Initial SessionFactory creation failed." + ex.getMessage());

			throw new ExceptionInInitializerError(ex);
		}
	}

	public SessionFactory getSessionFactory() {
		return sessionFactory;
	}
}
