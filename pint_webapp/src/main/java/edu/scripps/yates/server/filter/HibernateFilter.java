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

		final long numCall = ++numRPCCalls;
		log.info("Entering in the Hibernate filter in " + numCall + ". Num calls that are ongoing: " + numRPCCalls);
		String errorMessage = null;

		// /////
		// log.info("Creating a new session from Hibernate filter");
		// final Session session = ContextualSessionHandler.getSession();
		// log.info("Session id: " + session.hashCode());
		try {
			ContextualSessionHandler.printStatistics();
			ContextualSessionHandler.openSession();
			ContextualSessionHandler.beginGoodTransaction();
		} catch (Exception e) {
			e.printStackTrace();
			log.error(e);
		}
		try {
			// pass the request along the filter chain
			chain.doFilter(request, response);
			// ContextualSessionHandler.finishGoodTransaction();
		} catch (Exception ex) {
			log.warn(ex.getMessage());
			errorMessage = ex.getMessage();
			ex.printStackTrace();
			log.warn("Rolling back transaction");
			ContextualSessionHandler.rollbackTransaction();
			log.warn("Transaction rolled back");
			if (ServletException.class.isInstance(ex)) {
				throw (ServletException) ex;
			} else {
				throw new ServletException(ex);
			}
		} finally {
			numRPCCalls--;
			if (errorMessage == null) {
				log.info("Closing session from filter in Hibernate filter in call " + numCall + ". Num calls ongoing: "
						+ numRPCCalls);
			} else {
				log.warn("Closing session from filter in Hibernate filter in call " + numCall + ". Num calls ongoing: "
						+ numRPCCalls + " WITH ERROR: " + errorMessage);
			}
			try {
				// Close the Session
				ContextualSessionHandler.closeSession();

				ContextualSessionHandler.printStatistics();
			} catch (Exception e) {
				log.error(e);
			}
			// SessionPerKeyHandler.printStatistics();
			log.info("Session closed from filter in Hibernate filter in call " + numCall + ". Num calls ongoing: "
					+ numRPCCalls);
			// //////
		}
	}

	@Override
	public void init(FilterConfig fConfig) throws ServletException {
	}
}