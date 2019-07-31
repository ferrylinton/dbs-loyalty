package com.dbs.loyalty.util;

import com.dbs.loyalty.exception.NotFoundException;

public class ErrorUtil {
	
	public static final String PRODUCT = "Product";
	
	public static final String PROMO = "Promo";
	
	public static final String NOT_FOUND_FORMAT = "%s [id=%s] is not found";

	public static NotFoundException createNPE(String name, String id) {
		return new NotFoundException(String.format(NOT_FOUND_FORMAT, name, id));
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
