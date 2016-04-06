package edu.scripps.yates.server.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.apache.log4j.Logger;

public class ExceptionFilter implements Filter {
	private static Logger log = Logger.getLogger(ExceptionFilter.class);

	@Override
	public void destroy() {
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {

		try {

			// pass the request along the filter chain
			chain.doFilter(request, response);

		} catch (Exception ex) {
			ex.printStackTrace();
			log.error("ERROR catched in ExceptionFilter. Message: '"
					+ ex.getMessage() + "'");
		}
	}

	@Override
	public void init(FilterConfig fConfig) throws ServletException {
	}
}