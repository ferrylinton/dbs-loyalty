package com.dbs.loyalty.util;

import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;

public final class MessageUtil {

	private static MessageSource messageSource;

	public static void setMessageSource(MessageSource messageSource) {
		MessageUtil.messageSource = messageSource;
	}

	public static String getMessage(String code) {
		return messageSource.getMessage(code, null, code, LocaleContextHolder.getLocale());
	}

	public static String getMessage(String code, Object[] args) {
		return messageSource.getMessage(code, args, code, LocaleContextHolder.getLocale());
	}
	
	public static String getMessage(String code, Object arg) {
		Object[] args = new Object[] {arg};
		return messageSource.getMessage(code, args, code, LocaleContextHolder.getLocale());
	}
	
	public static String getMessage(String code, Object arg1, Object arg2) {
		Object[] args = new Object[] {arg1, arg2};
		return messageSource.getMessage(code, args, code, LocaleContextHolder.getLocale());
	}
	
	private MessageUtil() {
		// hide constructor
	}
	
}
