package edu.scripps.yates.server.util;

import javax.servlet.http.HttpServletRequest;

public final class HttpUtils {

	private static final String HEADER_X_FORWARDED_FOR = "X-FORWARDED-FOR";

	public static String remoteAddr(HttpServletRequest request) {
		String remoteAddr = request.getRemoteAddr();
		String x;
		if ((x = request.getHeader(HEADER_X_FORWARDED_FOR)) != null) {
			remoteAddr = x;
			int idx = remoteAddr.indexOf(',');
			if (idx > -1) {
				remoteAddr = remoteAddr.substring(0, idx);
			}
		}
		return remoteAddr;
	}

}
