package edu.scripps.yates.server.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.apache.log4j.Logger;

import edu.scripps.yates.proteindb.persistence.ContextualSessionHandler;

public class HibernateFilter implements Filter {
	private static Logger log = Logger.getLogger(HibernateFilter.class);
	private static long numRPCCalls = 0;

	@Override
	public void destroy() {
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		long numCall = 0;
		synchronized (this) {
			numCall = ++numRPCCalls;
		}

		try {
			log.debug(
					"Entering in the Hibernate filter in " + numCall + ". Num calls that are ongoing: " + numRPCCalls);

			// /////
			// log.info("Creating a new session from Hibernate filter");
			// final Session session = ContextualSessionHandler.getSession();
			// log.info("Session id: " + session.hashCode());
			try {
				ContextualSessionHandler.printStatistics();
				ContextualSessionHandler.beginGoodTransaction();
			} catch (final Exception e) {
				e.printStackTrace();
				log.error(e);
			}

			// pass the request along the filter chain
			chain.doFilter(request, response);
			// ContextualSessionHandler.finishGoodTransaction();
		} catch (final Exception ex) {
			ex.printStackTrace();
			log.warn("Rolling back transaction due to error: " + ex.getMessage());
			ContextualSessionHandler.rollbackTransaction();
			log.warn("Transaction rolled back");
			if (ServletException.class.isInstance(ex)) {
				throw (ServletException) ex;
			} else {
				throw new ServletException(ex);
			}
		} finally {
			synchronized (this) {
				numRPCCalls--;
			}

			log.debug("Closing session from filter in Hibernate filter in call " + numCall + ". Num calls ongoing: "
					+ numRPCCalls);

			try {
				// Close the Session
				ContextualSessionHandler.printStatistics();
				ContextualSessionHandler.closeSession();
				ContextualSessionHandler.printStatistics();
			} catch (final Exception e) {
				log.error("Error closing session: " + e.getMessage());
			}
			// SessionPerKeyHandler.printStatistics();
			log.debug("Session closed from filter in Hibernate filter in call " + numCall + ". Num calls ongoing: "
					+ numRPCCalls);
			// //////
		}
	}

	@Override
	public void init(FilterConfig fConfig) throws ServletException {
	}
}