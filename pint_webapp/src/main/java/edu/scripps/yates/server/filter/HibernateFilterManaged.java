package edu.scripps.yates.server.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.hibernate.FlushMode;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.StaleObjectStateException;
import org.hibernate.context.internal.ManagedSessionContext;
import org.hibernate.resource.transaction.spi.TransactionStatus;

import edu.scripps.yates.proteindb.persistence.HibernateUtil;

public class HibernateFilterManaged implements Filter {

	private static Logger log = Logger.getLogger(HibernateFilterManaged.class);

	private SessionFactory sf;

	public static final String HIBERNATE_SESSION_KEY = "hibernateSession";
	public static final String END_OF_CONVERSATION_FLAG = "endOfConversation";

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {

		org.hibernate.Session currentSession;

		// Try to get a Hibernate Session from the HttpSession
		HttpSession httpSession = ((HttpServletRequest) request).getSession();
		Session disconnectedSession = (Session) httpSession.getAttribute(HIBERNATE_SESSION_KEY);

		try {

			// Start a new conversation or in the middle?
			if (disconnectedSession == null) {
				log.info(">>> New conversation");
				currentSession = sf.openSession();
				currentSession.setFlushMode(FlushMode.NEVER);
			} else {
				log.info("< Continuing conversation");
				currentSession = disconnectedSession;
			}

			log.info("Binding the current Session");
			ManagedSessionContext.bind(currentSession);

			log.info("Starting a database transaction");
			currentSession.beginTransaction();

			log.info("Processing the event");
			chain.doFilter(request, response);

			log.info("Unbinding Session after processing");
			currentSession = ManagedSessionContext.unbind(sf);

			// End or continue the long-running conversation?
			if (request.getAttribute(END_OF_CONVERSATION_FLAG) != null
					|| request.getParameter(END_OF_CONVERSATION_FLAG) != null) {

				log.info("Flushing Session");
				currentSession.flush();

				log.info("Committing the database transaction");
				currentSession.getTransaction().commit();

				log.info("Closing the Session");
				currentSession.close();

				log.info("Cleaning Session from HttpSession");
				httpSession.setAttribute(HIBERNATE_SESSION_KEY, null);

				log.info("<<< End of conversation");

			} else {

				log.info("Committing database transaction");
				currentSession.getTransaction().commit();

				log.info("Storing Session in the HttpSession");
				httpSession.setAttribute(HIBERNATE_SESSION_KEY, currentSession);

				log.info("> Returning to user in conversation");
			}

		} catch (StaleObjectStateException staleEx) {
			log.error("This interceptor does not implement optimistic concurrency control!");
			log.error("Your application will not work until you add compensation actions!");
			// Rollback, close everything, possibly compensate for any permanent
			// changes
			// during the conversation, and finally restart business
			// conversation. Maybe
			// give the user of the application a chance to merge some of his
			// work with
			// fresh data... what you do here depends on your applications
			// design.
			throw staleEx;
		} catch (Throwable ex) {
			ex.printStackTrace();
			// Rollback only
			try {
				if (sf.getCurrentSession().getTransaction().getStatus() == TransactionStatus.ACTIVE) {
					log.info("Trying to rollback database transaction after exception");
					sf.getCurrentSession().getTransaction().rollback();
				}
			} catch (Throwable rbEx) {
				log.error("Could not rollback transaction after exception!", rbEx);
			} finally {
				log.error("Cleanup after exception!");

				// Cleanup
				log.info("Unbinding Session after exception");
				currentSession = ManagedSessionContext.unbind(sf);

				log.info("Closing Session after exception");
				currentSession.close();

				log.info("Removing Session from HttpSession");
				httpSession.setAttribute(HIBERNATE_SESSION_KEY, null);

			}

			// Let others handle it... maybe another interceptor for exceptions?
			throw new ServletException(ex);
		}

	}

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		log.info("Initializing filter...");
		log.info("Obtaining SessionFactory from static HibernateUtil singleton");
		sf = HibernateUtil.getInstance().getSessionFactory();
	}

	@Override
	public void destroy() {
	}

}
