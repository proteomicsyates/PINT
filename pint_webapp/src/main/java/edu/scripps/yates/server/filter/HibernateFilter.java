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

	@Override
	public void destroy() {
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		log.info("Entering in the Hibernate filter");
		String errorMessage = null;
		try {
			// /////
			// log.info("Creating a new session from Hibernate filter");
			// final Session session = ContextualSessionHandler.getSession();
			// log.info("Session id: " + session.hashCode());
			ContextualSessionHandler.beginGoodTransaction();
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
			if (errorMessage == null) {
				log.info("Closing session from filter in Hibernate filter");
			} else {
				log.warn("Closing session from filter in Hibernate filter WITH ERROR: " + errorMessage);
			}
			// Close the Session
			ContextualSessionHandler.closeSession();

			ContextualSessionHandler.printStatistics();
			// SessionPerKeyHandler.printStatistics();
			// log.info("Session closed from filter in Hibernate filter");
			// //////
		}
	}

	@Override
	public void init(FilterConfig fConfig) throws ServletException {
	}
}