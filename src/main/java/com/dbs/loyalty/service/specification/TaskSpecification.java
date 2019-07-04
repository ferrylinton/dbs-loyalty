package com.dbs.loyalty.service.specification;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.data.jpa.domain.Specification;

import com.dbs.loyalty.config.constant.Constant;
import com.dbs.loyalty.domain.Task;
import com.dbs.loyalty.domain.enumeration.TaskOperation;
import com.dbs.loyalty.domain.enumeration.TaskStatus;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class TaskSpecification {

	public static final String TASK_STATUS = "taskStatus";
	
	public static final String TASK_OPERATION = "taskOperation";
	
	public static final String TASK_DATA_TYPE = "taskDataType";
	
	public static final String ID = "id";
	
	public static final String MAKER = "maker";
	
	public static final String CHECKER = "checker";
	
	public static final String LIKE_FORMAT = "%%%s%%";
	
	public static final String OP_PARAM = "op";
	
	public static final String KY_PARAM = "ky";
	
	public static final String ST_PARAM = "st";
	
	public static Specification<Task> getSpec(String taskDataType, Map<String, String> params, HttpServletRequest request) {
		return Specification
				.where(taskDataType(taskDataType))
				.and(taskStatus(params, request))
				.and(taskOperation(params, request))
				.and(keyword(params));
	}
	
	public static Specification<Task> taskDataType(String taskDataType) {
		return (task, cq, cb) -> cb.equal(task.get(TASK_DATA_TYPE), taskDataType);
	}
	
	public static Specification<Task> taskStatus(Map<String, String> params, HttpServletRequest request) {
		TaskStatus taskStatus = getTaskStatus(params, request);
		
		if(taskStatus != TaskStatus.ALL) {
			return (task, cq, cb) -> cb.equal(task.get(TASK_STATUS), taskStatus);
		}else {
			return (task, cq, cb) -> cb.notEqual(task.get(TASK_STATUS), TaskStatus.ALL);
		}
	}
	
	public static Specification<Task> taskOperation(Map<String, String> params, HttpServletRequest request) {
		TaskOperation taskOperation = getTaskOperation(params, request);
		
		if(taskOperation != TaskOperation.ALL) {
			return (task, cq, cb) -> cb.equal(task.get(TASK_OPERATION), taskOperation);
		}else {
			return (task, cq, cb) -> cb.notEqual(task.get(TASK_OPERATION), TaskOperation.ALL);
		}
	}
	
	public static Specification<Task> keyword(Map<String, String> params) {
		if(params.get(KY_PARAM) != null && !Constant.EMPTY.equals(params.get(KY_PARAM).trim())) {
			String keyword = String.format(LIKE_FORMAT, params.get(KY_PARAM).trim().toLowerCase());
			return (task, cq, cb) -> cb.or(
						cb.like(cb.lower(task.get(MAKER)), keyword),
						cb.like(cb.lower(task.get(CHECKER)), keyword)
					);
		}else {
			return null;
		}
	}
	
	private static TaskStatus getTaskStatus(Map<String, String> params, HttpServletRequest request) {
		TaskStatus taskStatus = TaskStatus.PENDING;
		
		try {
			if(params.containsKey(ST_PARAM)) {
				taskStatus = TaskStatus.valueOf(params.get(ST_PARAM));
			}
		} catch (Exception e) {
			log.error(e.getLocalizedMessage(), e);
		}

		request.setAttribute(ST_PARAM, taskStatus);
		return taskStatus;
	}
	
	private static TaskOperation getTaskOperation(Map<String, String> params, HttpServletRequest request) {
		TaskOperation taskOperation = TaskOperation.ALL;
		
		try {
			if(params.containsKey(OP_PARAM)) {
				taskOperation = TaskOperation.valueOf(params.get(OP_PARAM));
			}
		} catch (Exception e) {;
			log.error(e.getLocalizedMessage(), e);
		}

		request.setAttribute(OP_PARAM, taskOperation);
		return taskOperation;
	}

	private TaskSpecification() {
		// hide constructor
	}
	
}