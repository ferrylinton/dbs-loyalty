package com.dbs.loyalty.web.controller.task;


import java.util.Optional;

import org.springframework.ui.ModelMap;

import com.dbs.loyalty.config.constant.Constant;
import com.dbs.loyalty.config.constant.DomainConstant;
import com.dbs.loyalty.config.constant.MessageConstant;
import com.dbs.loyalty.domain.Task;
import com.dbs.loyalty.service.TaskService;
import com.dbs.loyalty.util.MessageUtil;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class AbstractTaskController {

	protected static final String IS_CHECKER 			= "isChecker";
	
	protected static final String MADE_DATE 			= "madeDate";
	
	protected static final String TASK_VIEW_TEMPLATE	= "task/task-view";
	
	protected static final String TASK_DETAIL_TEMPLATE 	= "task/task-detail";
	
	protected static final String TASK_FORM_TEMPLATE 	= "task/task-form";
	
	protected final TaskService taskService;
	
	protected void view(String type, ModelMap model, String id) {
		Optional<Task> task = taskService.findById(id);
		
		if (task.isPresent()) {
			model.addAttribute(DomainConstant.TASK, task.get());
		} else {
			model.addAttribute(Constant.ERROR, MessageUtil.getNotFoundMessage(id));
		}
		
		model.addAttribute(DomainConstant.TYPE, type);
	}
	
	protected String save(Task task){
		try {
			String val = taskService.save(task);
			return getMessage(task, val);
		} catch (Exception ex) {
			log.error(ex.getLocalizedMessage(), ex);
			taskService.save(ex, task);
			return ex.getLocalizedMessage();
		}
	}
	
	protected String getMessage(Task task, String val) {
		Object[] args = new Object[] { MessageUtil.getMessage(task.getTaskOperation().toString()), MessageUtil.getMessage(task.getTaskDataType()), val };
		return MessageUtil.getMessage(task.getVerified() ? MessageConstant.TASK_IS_VERIFIED  : MessageConstant.TASK_IS_REJECTED, args);
	}

}
