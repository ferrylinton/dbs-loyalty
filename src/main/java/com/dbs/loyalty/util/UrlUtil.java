package com.dbs.loyalty.util;

public final class UrlUtil {

	public static final String RESOURCE_URL_FORMAT	= "%s/%s";
	
	public static final String TASK_URL_FORMAT	= "%s/task/%s";
	
	private static String contextPath;
	
	public static String getUrl(String resource) {
		return String.format(RESOURCE_URL_FORMAT, contextPath, resource);
	}

	public static String getTaskUrl(String type) {
		return String.format(TASK_URL_FORMAT, contextPath, type);
	}

	public static void setContextPath(String contextPath) {
		UrlUtil.contextPath = contextPath;
	}

	private UrlUtil() {
		// hide constructor
	}
	
}
