package com.dbs.loyalty.util;

import javax.servlet.http.HttpServletRequest;

import com.dbs.loyalty.config.constant.Constant;

public class UrlUtil {
	
	public static String getFullUrl(HttpServletRequest request) {
		StringBuilder requestURL = new StringBuilder(request.getRequestURL().toString());
		String queryString = request.getQueryString();
		
		if(queryString == null) {
			return requestURL.toString();
		}else {
			return requestURL.append(Constant.QUESTION).append(queryString).toString();
		}
		
	}
	
	private UrlUtil() {
		// hide constructor
	}
	
}
