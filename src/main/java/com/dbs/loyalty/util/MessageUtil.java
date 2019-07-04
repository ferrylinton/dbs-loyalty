package com.dbs.loyalty.util;

import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;

public final class MessageUtil {
	
	public static final String TASK_IS_SAVED = "message.taskIsSaved";
	
	public static final String DATA_WITH_VALUE_NOT_FOUND = "message.dataWithValueNotFound";

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

	public static String taskIsSavedMessage(String taskDataType, String val) {
		taskDataType = MessageUtil.getMessage(taskDataType);
		return MessageUtil.getMessage(TASK_IS_SAVED, taskDataType, val);
	}

	public static String getNotFoundMessage(String id) {
		return MessageUtil.getMessage(DATA_WITH_VALUE_NOT_FOUND, id);
	}
	
	private MessageUtil() {
		// hide constructor
	}
	
}
