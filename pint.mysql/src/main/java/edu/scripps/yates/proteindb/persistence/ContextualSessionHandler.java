package edu.scripps.yates.proteindb.persistence;

import java.io.Serializable;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.Hibernate;
import org.hibernate.HibernateException;
import org.hibernate.LockOptions;
import org.hibernate.Query;
import org.hibernate.ScrollMode;
import org.hibernate.ScrollableResults;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.context.internal.ManagedSessionContext;
import org.hibernate.criterion.Restrictions;
import org.hibernate.stat.Statistics;

/**
 * This class wrapes some functions for using a {@link Session} using the
 * approach of a Contextual Session, which means that the session is retrieved
 * by using the {@link SessionFactory} .getCurrentSession()<br>
 * Depending on the value of the property
 * 'hibernate.current_session_context_class' defined in file hibernate.cfg.xml
 * the session would be unique for each thread (thread), or will be managed
 * (managed), or JTA (jta), etc... <br>
 * See Contextual sessions
 * https://docs.jboss.org/hibernate/orm/3.3/reference/en/
 * html/architecture.html#architecture-current-session
 *
 * @author Salva
 *
 */
public class ContextualSessionHandler {

	private static SessionFactory sessionFactory;
	// private static final ThreadGroupLocal<Session> threadGroupSession = new
	// ThreadGroupLocal<Session>();

	private static Logger log = Logger.getLogger(ContextualSessionHandler.class);
	private static Statistics statistics;

	/**
	 * Setup the session factory with a propertiesFile that contains the username
	 * password and url for the connection to the db
	 *
	 * @param propertiesFile
	 * @return
	 */
	public static SessionFactory getSessionFactory(String dbUsername, String dbPassword, String dbURL) {
		if (sessionFactory == null) {
			sessionFactory = HibernateUtil.getInstance(dbUsername, dbPassword, dbURL).getSessionFactory();
			// enable statistics
			sessionFactory.getStatistics().setStatisticsEnabled(true);
		}
		return sessionFactory;
	}

	public static void clearSessionFactory() {
		HibernateUtil.clearSessionFactory();
		sessionFactory = null;
	}

	private static SessionFactory getSessionFactory() {
		return getSessionFactory(null, null, null);
	}

	/**
	 * query puede ser como: 'Select from Curso a order by a.titulo en realidad no
	 * es SQL es Hibernate Query Languaje (HQL)
	 */
	public static Query createQuery(String query) {
		return getCurrentSession().createQuery(query);
	}

	public static <T> Criteria createCriteria(Class<?> clazz) {
		return getCurrentSession().createCriteria(clazz);
	}

	public static <T> Criteria createCriteria(Class<?> clazz, String alias) {
		return getCurrentSession().createCriteria(clazz, alias);
	}

	public static <T> void saveSet(Set<T> set) {
		for (final T object : set) {
			getCurrentSession().save(object);
		}
	}

	public static <T> void deleteSet(Set<T> set) {
		if (set != null) {
			for (final T object : set) {
				getCurrentSession().delete(object);
			}
		}
	}

	/**
	 * Function that save a row in a table that is represented by an hibernate
	 * object
	 *
	 * @param        <T>
	 * @param object
	 * @return
	 */
	public static <T> Serializable save(T object) {
		final Serializable save = getCurrentSession().save(object);

		return save;

	}

	/**
	 * Function that update a row in a table that is represented by an hibernate
	 * object
	 *
	 * @param        <T>
	 * @param object
	 */
	public static <T> void update(T object) {
		getCurrentSession().update(object);
	}

	/**
	 * Function that update a row in a table that is represented by an hibernate
	 * object
	 *
	 * @param        <T>
	 * @param object
	 */
	public static <T> void saveOrUpdate(T object) {
		getCurrentSession().saveOrUpdate(object);
	}

	/**
	 * Function that delete a row from a table where object is a row of the table T
	 *
	 * @param        <T>
	 * @param object
	 */
	public static <T> void delete(T object) {
		if (object != null) {
			getCurrentSession().delete(object);
		}
	}

	public static <T> void refresh(T object) {
		getCurrentSession().refresh(object, LockOptions.READ);
	}

	public static <T> void refresh(Set<T> set) {
		for (final T object : set) {
			refresh(object);
		}

	}

	/**
	 * Function like Select * from clazz where primary_key = id
	 *
	 * @param       <T>
	 * @param id
	 * @param clazz hibernate class that corresponds to a table in the database
	 * @return the object loaded or null if it is not found in the database
	 */
	@SuppressWarnings("unchecked")
	public static <T> T load(Serializable id, Class<?> clazz) {
		if (id != null) {
			final T objectLoaded = (T) getCurrentSession().get(clazz, id);
			Hibernate.initialize(objectLoaded);
			return objectLoaded;
		}
		return null;
	}

	/**
	 * Function like select * from clazz where col1 == val1 col1 is a column of type
	 * M
	 *
	 * @param               <T>
	 * @param               <M>
	 * @param listParameter list of Parameter objects
	 * @param clazz
	 * @return the list of objects of the class = clazz
	 */
	public static <T, M> List<T> retrieveList(List<Parameter<M>> listParameter, Class<?> clazz) {
		final Criteria criteria = getCurrentSession().createCriteria(clazz);
		for (final Parameter<?> parameter : listParameter) {
			criteria.add(Restrictions.eq(parameter.getKey(), parameter.getValue()));
		}
		@SuppressWarnings("unchecked")
		final List<T> result = criteria.list();

		return result;
	}

	/**
	 * Function like select * from clazz
	 *
	 * @param       <T>
	 *
	 * @param clazz
	 * @return the list of objects of the class = clazz
	 */
	public static <T> List<T> retrieveList(Class<?> clazz) {
		final Criteria criteria = getCurrentSession().createCriteria(clazz);

		@SuppressWarnings("unchecked")
		final List<T> result = criteria.list();

		return result;
	}

	/**
	 * Function like select * from clazz
	 *
	 * @param       <T>
	 *
	 * @param clazz
	 * @return the list of objects of the class = clazz
	 */
	public static <T> ScrollableResults retrieveReadOnlyIterator(Class<?> clazz) {
		final Criteria criteria = getCurrentSession().createCriteria(clazz).setReadOnly(true);

		@SuppressWarnings("unchecked")
		final ScrollableResults result = criteria.scroll(ScrollMode.FORWARD_ONLY);

		return result;
	}

	/**
	 * Function like select * from clazz
	 *
	 * @param          <T>
	 *
	 * @param clazz
	 * @param readonly
	 * @return the list of objects of the class = clazz
	 */
	public static <T> ScrollableResults retrieveIterator(Class<?> clazz, int fetchSize, boolean readonly) {
		final Criteria criteria = getCurrentSession().createCriteria(clazz).setReadOnly(readonly)
				.setFetchSize(fetchSize);

		@SuppressWarnings("unchecked")
		final ScrollableResults result = criteria.scroll(ScrollMode.FORWARD_ONLY);
		return result;
	}

	/**
	 * Function like select * from clazz
	 *
	 * @param       <T>
	 *
	 * @param clazz
	 * @return the list of objects of the class = clazz
	 */
	public static <T> ScrollableResults retrieveIterator(Class<?> clazz) {
		return retrieveIterator(clazz, 1000, false);
	}

	/**
	 * Function like "select * from clazz where col1 == val1 and col2 == val2" col1
	 * is a <M1> type column and col2 is a <M2> type column
	 *
	 * @param                <T>
	 * @param                <M2>
	 * @param                <M1>
	 * @param listParameter  :list of <M1> type values for the col1
	 * @param listParameter2 :list of <M2> type values for the col2
	 * @param clazz
	 * @return a list of objects from class T retrieved from the database
	 */
	public static <T, M2, M1> List<T> retrieveList(List<Parameter<M1>> listParameter,
			List<Parameter<M2>> listParameter2, Class<?> clazz) {
		final Criteria criteria = getCurrentSession().createCriteria(clazz);
		for (final Parameter<?> parameter : listParameter) {
			criteria.add(Restrictions.eq(parameter.getKey(), parameter.getValue()));
		}
		for (final Parameter<?> parameter : listParameter2) {
			criteria.add(Restrictions.eq(parameter.getKey(), parameter.getValue()));
		}
		return criteria.list();
	}

	/**
	 * Function like "select * from clazz where col1 == val1 and col2 == val2" col1
	 * is a <M1> type column and col2 is a <M2> type column
	 *
	 * @param                <M3>
	 * @param                <M1>
	 * @param                <T>
	 * @param                <M2>
	 * @param listParameter  :list of <M1> type values for the col1
	 * @param listParameter2 :list of <M2> type values for the col2
	 * @param listParameter3 :list of <M3> type values for the col3
	 * @param clazz
	 * @return a list of objects from class T retrieved from the database
	 */
	public static <M1, M2, M3, T> List<T> retrieveList(List<Parameter<M1>> listParameter,
			List<Parameter<M2>> listParameter2, List<Parameter<M3>> listParameter3, Class<?> clazz) {
		final Criteria criteria = getCurrentSession().createCriteria(clazz);
		for (final Parameter<?> parameter : listParameter) {
			criteria.add(Restrictions.eq(parameter.getKey(), parameter.getValue()));
		}
		for (final Parameter<?> parameter : listParameter2) {
			criteria.add(Restrictions.eq(parameter.getKey(), parameter.getValue()));
		}
		for (final Parameter<?> parameter : listParameter3) {
			criteria.add(Restrictions.eq(parameter.getKey(), parameter.getValue()));
		}
		return criteria.list();
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

	public static void finishGoodTransaction() {
		commitTransaction();
	}

	// public void rollBackTransaction() {
	// session.getTransaction().rollback();
	//
	// }

	// public void closeSession() {
	// session.close();
	// }

	public static Session getCurrentSession() {
		Session currentSession = null;
		try {
			currentSession = getSessionFactory().getCurrentSession();

		} catch (final HibernateException e) {
			currentSession = openSession();
		}

		return currentSession;
	}

	public static void closeSession() {

		// log.info("Closing the session " + contador + " (closing)" +
		// " from Thread: " + Thread.currentThread().getId());
		final Session session = getCurrentSession();
		ManagedSessionContext.unbind(getSessionFactory());
		session.close();

	}

	public static void beginGoodTransaction() {
		getCurrentSession().beginTransaction();

	}

	private static void commitTransaction() {
		getCurrentSession().getTransaction().commit();
	}

	public static void rollbackTransaction() {

		getCurrentSession().getTransaction().rollback();
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

		if (getSessionFactory() != null) {
			ContextualSessionHandler.statistics = getSessionFactory().getStatistics();
			if (statistics != null) {
				log.info("Sessions: " + statistics.getSessionOpenCount() + ", OPEN="
						+ (statistics.getSessionOpenCount() - statistics.getSessionCloseCount()) + ", Transactions="
						+ statistics.getTransactionCount() + ", Connections=" + statistics.getConnectCount());

				// log.info(statistics);
			}
		} else {
			log.info("sessionFactory is NULL!!");
		}
	}

	public static long getNumSessionsClosed() {
		if (getSessionFactory() != null) {
			ContextualSessionHandler.statistics = getSessionFactory().getStatistics();
			if (statistics != null) {
				return statistics.getSessionCloseCount();
			}
		}
		return 0;
	}

	public static long getNumSessionsOpened() {
		if (getSessionFactory() != null) {
			ContextualSessionHandler.statistics = getSessionFactory().getStatistics();
			if (statistics != null) {
				return statistics.getSessionOpenCount();
			}
		}
		return 0;
	}

	/**
	 * Force this session to flush. Must be called at the end of a unit of work,
	 * before committing the transaction and closing the session (depending on
	 * setFlushMode(FlushMode), Transaction.commit() calls this method).
	 *
	 * Flushing is the process of synchronizing the underlying persistent store with
	 * persistable state held in memory.
	 *
	 */
	public static void flush() {
		getCurrentSession().flush();
	}

	/**
	 * Completely clear the session. Evict all loaded instances and cancel all
	 * pending saves, updates and deletions. Do not close open iterators or
	 * instances of ScrollableResults.
	 *
	 */
	public static void clear() {
		getCurrentSession().clear();
	}

	/**
	 * This open a session, but it will fail if the session factory was not created
	 * before by calling to openSession(username, password, dbURL)
	 * 
	 * @return
	 */
	public static Session openSession() {
		final Session currentSession = getSessionFactory().openSession();
		ManagedSessionContext.bind(currentSession);
		return currentSession;

	}

	/**
	 * Opens a session and if it is necessary creates a new sessionFactory using the
	 * username password and dbURL provided.
	 * 
	 * @param username
	 * @param password
	 * @param dbURL
	 * @return
	 */
	public static Session openSession(String username, String password, String dbURL) {
		final Session currentSession = getSessionFactory(username, password, dbURL).openSession();
		ManagedSessionContext.bind(currentSession);
		return currentSession;

	}

}
