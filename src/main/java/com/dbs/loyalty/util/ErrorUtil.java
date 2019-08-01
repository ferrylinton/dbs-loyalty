package com.dbs.loyalty.util;

import com.dbs.loyalty.exception.NotFoundException;

public class ErrorUtil {
	
	public static final String PRODUCT = "Product";

	public static final String PROMO = "Promo";
	
	public static final String NOT_FOUND_FORMAT = "%s [id=%s] is not found";
	
	public static final String INVALID_DATA_FORMAT = "Invalid data [%s should be %s]";

	public static NotFoundException createNPE(String name, String id) {
		return new NotFoundException(getNotFoundMessage(name, id));
	}
	
	public static String getNotFoundMessage(String name, String id) {
		return String.format(NOT_FOUND_FORMAT, name, id);
	}
	
	public static String getIvalidDataMessage(String name, String value) {
		return String.format(INVALID_DATA_FORMAT, name, value);
	}
	
	public static String getErrorMessage(Throwable ex) {
		ex = getThrowable(ex);
		return (ex != null) ? ex.getMessage() : null;
	}
	
	public static Throwable getThrowable(Throwable ex) {
		if(ex != null && ex.getCause() != null) {
			ex = getThrowable(ex.getCause());
		}
		
		return ex;
	}
	
	private ErrorUtil() {
		// hide constructor
	}
	
}
