package edu.scripps.yates.proteindb.persistence;

import java.io.Serializable;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.Hibernate;
import org.hibernate.HibernateException;
import org.hibernate.LockMode;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;
import org.hibernate.resource.transaction.spi.TransactionStatus;
import org.hibernate.stat.Statistics;

/**
 * This class handles the {@link Session} and a {@link Transaction} by storing
 * it on a normal variable.
 *
 * @author Salva
 *
 */
public class SessionPerRequestHandler {
	// TODO CHANGE THIS USENAME, PASSW, URL if you want to use it
	private static final SessionFactory sessionFactory = HibernateUtil.getInstance(null, null, null)
			.getSessionFactory();
	private Session session;
	// private static final ThreadGroupLocal<Session> threadGroupSession = new
	// ThreadGroupLocal<Session>();
	private Transaction tx;

	private static Logger log = Logger.getLogger("log4j.logger.org.proteored");
	private static int contador = 0;
	private static Statistics statistics;

	public SessionPerRequestHandler() {

	}

	/**
	 * Enable statistics
	 */
	static {
		sessionFactory.getStatistics().setStatisticsEnabled(true);
	}

	/**
	 * query puede ser como: 'Select from Curso a order by a.titulo en realidad
	 * no es SQL es Hibernate Query Languaje (HQL)
	 */
	public Query createQuery(String query) {
		return getSession().createQuery(query);
	}

	public <T> Criteria createCriteria(Class<?> clazz) {
		return getSession().createCriteria(clazz);
	}

	public <T> void saveSet(Set<T> set) {
		for (T object : set) {
			getSession().save(object);
		}
	}

	public <T> void deleteSet(Set<T> set) {
		if (set != null) {
			for (T object : set) {
				getSession().delete(object);
			}
		}
	}

	/**
	 * Function that save a row in a table that is represented by an hibernate
	 * object
	 *
	 * @param <T>
	 * @param object
	 * @return
	 */
	public <T> Serializable save(T object) {
		final Serializable save = getSession().save(object);

		return save;

	}

	/**
	 * Function that update a row in a table that is represented by an hibernate
	 * object
	 *
	 * @param <T>
	 * @param object
	 */
	public <T> void update(T object) {
		getSession().update(object);
	}

	/**
	 * Function that update a row in a table that is represented by an hibernate
	 * object
	 *
	 * @param <T>
	 * @param object
	 */
	public <T> void saveOrUpdate(T object) {
		getSession().saveOrUpdate(object);
	}

	/**
	 * Function that delete a row from a table where object is a row of the
	 * table T
	 *
	 * @param <T>
	 * @param object
	 */
	public <T> void delete(T object) {
		if (object != null) {
			getSession().delete(object);
		}
	}

	public <T> void refresh(T object) {
		getSession().refresh(object, LockMode.READ);
	}

	public <T> void refresh(Set<T> set) {
		for (T object : set) {
			refresh(object);
		}

	}

	/**
	 * Function like Select * from clazz where primary_key = id
	 *
	 * @param <T>
	 * @param id
	 * @param clazz
	 *            hibernate class that corresponds to a table in the database
	 * @return the object loaded or null if it is not found in the database
	 */
	@SuppressWarnings("unchecked")
	public <T> T load(Serializable id, Class<?> clazz) {
		T objectLoaded = (T) getSession().get(clazz, id);
		Hibernate.initialize(objectLoaded);
		return objectLoaded;
	}

	/**
	 * Function like select * from clazz where col1 == val1 col1 is a column of
	 * type M
	 *
	 * @param <T>
	 * @param <M>
	 * @param listParameter
	 *            list of Parameter objects
	 * @param clazz
	 * @return the list of objects of the class = clazz
	 */
	public <T, M> List<T> retrieveList(List<Parameter<M>> listParameter, Class<?> clazz) {
		Criteria criteria = getSession().createCriteria(clazz);
		for (Parameter<?> parameter : listParameter) {
			criteria.add(Restrictions.eq(parameter.getKey(), parameter.getValue()));
		}
		@SuppressWarnings("unchecked")
		List<T> result = criteria.list();

		return result;
	}

	/**
	 * Function like select * from clazz
	 *
	 * @param <T>
	 *
	 * @param clazz
	 * @return the list of objects of the class = clazz
	 */
	public <T> List<T> retrieveList(Class<?> clazz) {
		Criteria criteria = getSession().createCriteria(clazz);

		@SuppressWarnings("unchecked")
		List<T> result = criteria.list();

		return result;
	}

	/**
	 * Function like "select * from clazz where col1 == val1 and col2 == val2"
	 * col1 is a <M1> type column and col2 is a <M2> type column
	 *
	 * @param <T>
	 * @param <M2>
	 * @param <M1>
	 * @param listParameter
	 *            :list of <M1> type values for the col1
	 * @param listParameter2
	 *            :list of <M2> type values for the col2
	 * @param clazz
	 * @return a list of objects from class T retrieved from the database
	 */
	public <T, M2, M1> List<T> retrieveList(List<Parameter<M1>> listParameter, List<Parameter<M2>> listParameter2,
			Class<?> clazz) {
		Criteria criteria = getSession().createCriteria(clazz);
		for (Parameter<?> parameter : listParameter) {
			criteria.add(Restrictions.eq(parameter.getKey(), parameter.getValue()));
		}
		for (Parameter<?> parameter : listParameter2) {
			criteria.add(Restrictions.eq(parameter.getKey(), parameter.getValue()));
		}
		return criteria.list();
	}

	/**
	 * Function like "select * from clazz where col1 == val1 and col2 == val2"
	 * col1 is a <M1> type column and col2 is a <M2> type column
	 *
	 * @param <M3>
	 * @param <M1>
	 * @param <T>
	 * @param <M2>
	 * @param listParameter
	 *            :list of <M1> type values for the col1
	 * @param listParameter2
	 *            :list of <M2> type values for the col2
	 * @param listParameter3
	 *            :list of <M3> type values for the col3
	 * @param clazz
	 * @return a list of objects from class T retrieved from the database
	 */
	public <M1, M2, M3, T> List<T> retrieveList(List<Parameter<M1>> listParameter, List<Parameter<M2>> listParameter2,
			List<Parameter<M3>> listParameter3, Class<?> clazz) {
		Criteria criteria = getSession().createCriteria(clazz);
		for (Parameter<?> parameter : listParameter) {
			criteria.add(Restrictions.eq(parameter.getKey(), parameter.getValue()));
		}
		for (Parameter<?> parameter : listParameter2) {
			criteria.add(Restrictions.eq(parameter.getKey(), parameter.getValue()));
		}
		for (Parameter<?> parameter : listParameter3) {
			criteria.add(Restrictions.eq(parameter.getKey(), parameter.getValue()));
		}
		return criteria.list();
	}

	public void beginTransaction() {
		// session.beginTransaction();
	}

	public void finishReadTransaction() {
		// session.getTransaction().commit();

	}

	// public void finishReadTransaction() {
	// // System.out.println("Omitting finishReadTransaction");
	//
	// // commitTransaction();
	// }

	// public void finishTransaction() {
	// session.getTransaction().commit();
	//
	// }

	public void finishGoodTransaction() {
		commitTransaction();
	}

	// public void rollBackTransaction() {
	// session.getTransaction().rollback();
	//
	// }
	public void rollBackTransaction() {
		log.debug("Rolling back transaction");
		rollbackTransaction();

	}

	// public void closeSession() {
	// session.close();
	// }

	public synchronized Session getSession() {
		return openSession();
	}

	public Session openSession() {
		try {
			if (session == null) {
				log.debug("Opening a new session (getting)");
				log.debug("This is the session number:" + contador++);
				session = sessionFactory.openSession();
			}
			// log.debug("Getting session " + s.hashCode() + " from Thread: "
			// + Thread.currentThread().getId());
		} catch (HibernateException ex) {
			throw new IllegalArgumentException(ex);
		}
		return session;
	}

	public void closeSession() {
		log.debug("Closing the session " + contador + " (closing)" + " from Thread: " + Thread.currentThread().getId());

		if (session != null && session.isOpen()) {
			log.debug("The session number " + contador + " is going to be closed" + " from Thread: "
					+ Thread.currentThread().getId());
			contador--;
			session.clear();
			log.debug("Session cleared");
			session.close();
			log.debug("Session closed in finally block");
			session = null;
		}

	}

	public synchronized void beginGoodTransaction() {

		try {
			if (tx == null) {
				tx = getSession().beginTransaction();
				if (tx != null)
					log.debug("Begin new transaction: " + tx.hashCode() + " from Thread: "
							+ Thread.currentThread().getId());
				else
					log.debug("Transaction is NULL when begin transaction is called from Thread: "
							+ Thread.currentThread().getId());
			} else {
				log.debug(
						"Begin old transaction: " + tx.hashCode() + " from Thread: " + Thread.currentThread().getId());
			}
		} catch (HibernateException ex) {
			throw new IllegalArgumentException(ex);
		}
	}

	private synchronized void commitTransaction() {

		try {
			// if (tx != null && !tx.wasCommitted() && !tx.wasRolledBack()) {
			if (tx != null && tx.getStatus() != TransactionStatus.COMMITTED
					&& tx.getStatus() != TransactionStatus.ROLLED_BACK) {
				tx.commit();
				log.debug("Commit success from Thread: " + Thread.currentThread().getId());
			}
			tx = null;

			log.debug("Transaction to commit is NULL from ThreadLocal variable!");
		} catch (HibernateException ex) {
			rollbackTransaction();
			throw new IllegalArgumentException(ex);
		}
	}

	public synchronized void rollbackTransaction() {

		if (tx != null)
			log.debug("Rolling bak transaction :" + tx.hashCode() + " from Thread: " + Thread.currentThread().getId());
		else
			log.debug("Transaction to roolback is NULL from ThreadLocal variable!");
		try {
			// if (tx != null && !tx.wasCommitted() && !tx.wasRolledBack()) {
			if (tx != null && tx.getStatus() != TransactionStatus.COMMITTED
					&& tx.getStatus() != TransactionStatus.ROLLED_BACK) {
				tx.rollback();
				log.debug("Rolling bak success from Thread: " + Thread.currentThread().getId());
			}
			tx = null;
		} catch (HibernateException ex) {
			throw new IllegalArgumentException(ex);
		} finally {
			closeSession();
		}
	}

	// /////////////////////////////////////////

	// public static void reconnect(Session session) throws
	// IllegalMiapeArgumentException {
	// try {
	// session.reconnect();
	// threadGroupSession.setValue(session);
	// } catch (HibernateException ex) {
	// throw new IllegalMiapeArgumentException(ex);
	// }
	// }
	//
	// public static Session disconnectSession() throws
	// IllegalMiapeArgumentException {
	// Session session = getSession();
	// try {
	// threadGroupSession.setValue(null);
	// if (session.isConnected() && session.isOpen())
	// session.disconnect();
	// } catch (HibernateException ex) {
	// throw new IllegalMiapeArgumentException(ex);
	// }
	// return session;
	// }

	// public static void newApplicationTx() {
	// closeSession();
	// }

	public static void printStatistics() {
		log.info("Session statistics");
		if (sessionFactory != null) {
			SessionPerRequestHandler.statistics = sessionFactory.getStatistics();
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
			SessionPerRequestHandler.statistics = sessionFactory.getStatistics();
			if (statistics != null) {
				return statistics.getSessionCloseCount();
			}
		}
		return 0;
	}

	public static long getNumSessionsOpened() {
		if (sessionFactory != null) {
			SessionPerRequestHandler.statistics = sessionFactory.getStatistics();
			if (statistics != null) {
				return statistics.getSessionOpenCount();
			}
		}
		return 0;
	}

}
