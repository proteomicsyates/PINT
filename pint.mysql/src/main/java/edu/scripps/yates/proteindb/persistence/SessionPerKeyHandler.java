package edu.scripps.yates.proteindb.persistence;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.hibernate.SessionFactory;
import org.hibernate.stat.Statistics;

public class SessionPerKeyHandler {
	private static final Logger log = Logger.getLogger(SessionPerKeyHandler.class);
	private static final Map<String, SessionPerKey> sessions = new HashMap<String, SessionPerKey>();
	private static final SessionFactory sessionFactory = HibernateUtil.getInstance(null).getSessionFactory();

	/**
	 * Enable statistics
	 */
	static {
		sessionFactory.getStatistics().setStatisticsEnabled(true);
	}

	public synchronized static SessionPerKey getSessionPerKey(String sessionKey) {
		if (sessions.containsKey(sessionKey)) {
			final SessionPerKey sessionPerKey = sessions.get(sessionKey);
			return sessionPerKey;
		} else {
			SessionPerKey sessionPerKey = new SessionPerKey(sessionKey);
			sessions.put(sessionKey, sessionPerKey);
			return sessionPerKey;
		}
	}

	public static void printStatistics() {
		log.info("Session statistics");
		if (sessionFactory != null) {
			Statistics statistics = sessionFactory.getStatistics();
			if (statistics != null) {
				log.info("Sessions closed: " + statistics.getSessionCloseCount());
				log.info("Sessions opened: " + statistics.getSessionOpenCount());
				log.info("Transactions: " + statistics.getTransactionCount());

				log.info(statistics);
			}
		} else {
			log.info("sessionFactory is NULL!!");
		}
		log.info("End Session statistics");
	}

	public static long getNumSessionsClosed() {
		if (sessionFactory != null) {
			Statistics statistics = sessionFactory.getStatistics();
			if (statistics != null) {
				return statistics.getSessionCloseCount();
			}
		}
		return 0;
	}

	public static long getNumSessionsOpened() {
		if (sessionFactory != null) {
			Statistics statistics = sessionFactory.getStatistics();
			if (statistics != null) {
				return statistics.getSessionOpenCount();
			}
		}
		return 0;
	}

}
