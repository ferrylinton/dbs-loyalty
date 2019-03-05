package com.dbs.loyalty.util;

public class ErrorUtil {

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
	
}
