package com.dbs.loyalty.web.controller;

import com.dbs.loyalty.util.MessageUtil;

public abstract class AbstractController {

	public static final String TASK_IS_SAVED = "message.taskIsSaved";
	
	public static final String DATA_WITH_VALUE_NOT_FOUND = "message.dataWithValueNotFound";
	
	protected String taskIsSavedMessage(String taskDataType, String val) {
		taskDataType = MessageUtil.getMessage(taskDataType);
		return MessageUtil.getMessage(TASK_IS_SAVED, taskDataType, val);
	}

	protected String getNotFoundMessage(String id) {
		return MessageUtil.getMessage(DATA_WITH_VALUE_NOT_FOUND, id);
	}

}
