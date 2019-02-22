package com.dbs.priviledge.util;

public class UrlUtil {

	public static final String ENTITY_URL_FORMAT	= "%s/secure/%s";
	
	public static String contextPath;

	public static String getUrl(String urlFormat, String entityName) {
		return String.format(urlFormat, contextPath, entityName);
	}

	public static String getEntityUrl(String entityName) {
		return getUrl(ENTITY_URL_FORMAT, entityName);
	}

}
