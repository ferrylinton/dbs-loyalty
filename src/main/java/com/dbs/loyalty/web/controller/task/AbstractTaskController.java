package com.dbs.loyalty.web.controller.task;

import static com.dbs.loyalty.config.constant.Constant.ERROR;
import static com.dbs.loyalty.config.constant.EntityConstant.TASK;
import static com.dbs.loyalty.config.constant.EntityConstant.TYPE;
import static com.dbs.loyalty.config.constant.MessageConstant.TASK_IS_REJECTED;
import static com.dbs.loyalty.config.constant.MessageConstant.TASK_IS_VERIFIED;

import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.ui.ModelMap;

import com.dbs.loyalty.service.TaskService;
import com.dbs.loyalty.service.dto.TaskDto;
import com.dbs.loyalty.util.MessageUtil;
import com.dbs.loyalty.util.UrlUtil;
import com.dbs.loyalty.web.controller.AbstractPageController;
import com.dbs.loyalty.web.response.AbstractResponse;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class AbstractTaskController extends AbstractPageController {

	protected static final String IS_CHECKER = "isChecker";
	
	protected static final String MADE_DATE = "madeDate";
	
	protected static final String TASK_VIEW_TEMPLATE = "task/task-view";
	
	protected static final String TASK_DETAIL_TEMPLATE = "task/task-detail";
	
	protected static final String TASK_FORM_TEMPLATE = "task/task-form";
	
	protected final TaskService taskService;
	
	protected void view(String type, ModelMap model, String id) {
		Optional<TaskDto> task = taskService.findById(id);
		
		if (task.isPresent()) {
			model.addAttribute(TASK, task.get());
		} else {
			model.addAttribute(ERROR, getNotFoundMessage(id));
		}
		
		model.addAttribute(TYPE, type);
	}
	
	protected ResponseEntity<AbstractResponse> save(TaskDto taskDto){
		try {
			String val = taskService.save(taskDto);
			String resultUrl = UrlUtil.getTaskUrl(taskDto.getTaskDataType());
			return dataIsSavedResponse(getMessage(taskDto, val), resultUrl);
		} catch (Exception ex) {
			log.error(ex.getLocalizedMessage(), ex);
			taskService.save(ex, taskDto);
			return errorResponse(ex);
		}
	}
	
	protected String getMessage(TaskDto taskDto, String val) {
		Object[] args = new Object[] { MessageUtil.getMessage(taskDto.getTaskOperation().toString()), MessageUtil.getMessage(taskDto.getTaskDataType()), val };
		return MessageUtil.getMessage(taskDto.isVerified() ? TASK_IS_VERIFIED  : TASK_IS_REJECTED, args);
	}

}
