package com.dbs.loyalty.util;

public class UrlUtil {

	public static final String RESOURCE_URL_FORMAT	= "%s/%s";
	
	public static final String TASK_URL_FORMAT	= "%s/%s/%s";
	
	public static String contextPath;
	
	public static String getUrl(String resource) {
		return String.format(RESOURCE_URL_FORMAT, contextPath, resource);
	}

	public static String getTaskUrl(String resource, String type) {
		return String.format(TASK_URL_FORMAT, contextPath, resource, type);
	}
	
}
