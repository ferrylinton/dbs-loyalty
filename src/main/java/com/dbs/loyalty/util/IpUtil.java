package com.dbs.loyalty.util;

import javax.servlet.http.HttpServletRequest;

public class IpUtil {

	private static IpUtil instance;
	
	private final String UNKNOWN = "unknown";

	private final String IP_ADDRESS_HEADER_1 = "X-Forwarded-For";

	private final String IP_ADDRESS_HEADER_2 = "X_FORWARDED_FOR";
	
	private IpUtil() {}
	
	public static IpUtil getInstance() {
		if(instance == null) {
			instance = new IpUtil();
		}
		
		return instance;
	}
	
	public String getIp(HttpServletRequest request) {
		String ipAddress = request.getHeader(IP_ADDRESS_HEADER_1);

		if (ipAddress == null) {
			ipAddress = request.getHeader(IP_ADDRESS_HEADER_2);

			if (ipAddress == null) {
				ipAddress = request.getRemoteAddr();
			}
		}

		return ipAddress == null ? UNKNOWN : ipAddress;
	}
	
}
