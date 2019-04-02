package com.dbs.loyalty.util;

import javax.servlet.http.HttpServletRequest;

public class IpUtil {

	private static final String UNKNOWN = "unknown";

	private static final String IP_ADDRESS_HEADER_1 = "X-Forwarded-For";

	private static final String IP_ADDRESS_HEADER_2 = "X_FORWARDED_FOR";
 
	public static String getIp(HttpServletRequest request) {
		String ipAddress = request.getHeader(IP_ADDRESS_HEADER_1);

		if (ipAddress == null) {
			ipAddress = request.getHeader(IP_ADDRESS_HEADER_2);

			if (ipAddress == null) {
				ipAddress = request.getRemoteAddr();
			}
		}

		return ipAddress == null ? UNKNOWN : ipAddress;
	}
	
	private IpUtil() {
		// hide constructor
	}
	
}
