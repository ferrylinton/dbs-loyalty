package com.dbs.loyalty.util;

import javax.servlet.http.HttpServletRequest;

public class UrlUtil {
	
	public static String getFullUrl(HttpServletRequest httpServletRequest) {
		StringBuilder requestURL = new StringBuilder(httpServletRequest.getRequestURL().toString());
		String queryString = httpServletRequest.getQueryString();
		
		if(queryString == null) {
			return requestURL.toString();
		}else {
			return requestURL.append("?").append(queryString).toString();
		}
		
	}
	
	private UrlUtil() {
		// hide constructor
	}
	
}
