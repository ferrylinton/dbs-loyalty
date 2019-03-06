package com.dbs.loyalty.util;

public class UrlUtil {

	public static final String RESOURCE_URL_FORMAT	= "%s/%s";
	
	public static String contextPath;

	public static String getUrl(String urlFormat, String resource) {
		return String.format(urlFormat, contextPath, resource);
	}
	
	public static String getyUrl(String resource) {
		return getUrl(RESOURCE_URL_FORMAT, resource);
	}

	public static String getEntityUrl(String resource) {
		return getUrl(RESOURCE_URL_FORMAT, resource);
	}

}
