package com.dbs.loyalty.util;

import java.util.Map;

import com.dbs.loyalty.config.constant.Constant;
import com.dbs.loyalty.enumeration.TaskOperation;
import com.dbs.loyalty.enumeration.TaskStatus;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class TaskUtil {

	public static TaskStatus getTaskStatus(Map<String, String> params) {
		try {
			if(params.containsKey(Constant.ST_PARAM)) {
				return TaskStatus.valueOf(params.get(Constant.ST_PARAM));
			}
		} catch (Exception e) {
			log.error(e.getLocalizedMessage(), e);
		}
		
		return TaskStatus.PENDING;
	}
	
	public static TaskOperation getTaskOperation(Map<String, String> params) {
		try {
			if(params.containsKey(Constant.OP_PARAM)) {
				return TaskOperation.valueOf(params.get(Constant.OP_PARAM));
			}
		} catch (Exception e) {;
			log.error(e.getLocalizedMessage(), e);
		}
		
		return TaskOperation.ALL;
	}
	
	private TaskUtil() {
		// hide constructor
	}
	
}
