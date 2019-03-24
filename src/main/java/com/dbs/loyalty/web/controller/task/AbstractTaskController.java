package com.dbs.loyalty.web.controller.task;

import static com.dbs.loyalty.config.constant.Constant.ERROR;
import static com.dbs.loyalty.config.constant.Constant.PAGE;
import static com.dbs.loyalty.config.constant.EntityConstant.*;

import java.util.Map;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.ModelMap;

import com.dbs.loyalty.exception.NotFoundException;
import com.dbs.loyalty.service.TaskService;
import com.dbs.loyalty.service.dto.TaskDto;
import com.dbs.loyalty.util.ErrorUtil;
import com.dbs.loyalty.util.UrlUtil;
import com.dbs.loyalty.web.controller.AbstractPageController;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class AbstractTaskController extends AbstractPageController {

	private String REDIRECT 		= "redirect:/task";

	private String VIEW_TEMPLATE 	= "task/view";

	private String FORM_TEMPLATE 	= "task/form";

	private String SORT_BY 			= "madeDate";
	
	private Order ORDER				= Order.asc(SORT_BY).ignoreCase();

	protected final TaskService taskService;
	
	protected String view(String type, Map<String, String> params, Sort sort, HttpServletRequest request) {
		Order order = (sort.getOrderFor(SORT_BY) == null) ? ORDER : sort.getOrderFor(SORT_BY);
		Page<TaskDto> page = taskService.findAll(type, params, getPageable(params, order), request);
		
		if (page.getNumber() > 0 && page.getNumber() + 1 > page.getTotalPages()) {
			return REDIRECT;
		}
		
		request.setAttribute(PAGE, page);
		request.setAttribute(TYPE, type);
		setParamsQueryString(params, request);
		setPagerQueryString(order, page.getNumber(), request);
		return VIEW_TEMPLATE;
	}
	
	protected String view(String type, ModelMap model, String id) throws NotFoundException {
		Optional<TaskDto> task = taskService.findById(id);
		
		if (task.isPresent()) {
			model.addAttribute(TASK, task.get());
		} else {
			model.addAttribute(ERROR, getNotFoundMessage(id));
		}
		
		model.addAttribute(TYPE, type);
		return FORM_TEMPLATE;
	}
	
	protected ResponseEntity<?> save(TaskDto taskDto){
		try {
			String val = taskService.save(taskDto);
			String resultUrl = UrlUtil.getTaskUrl(taskDto.getTaskDataType());
			return saveResponse(getMessage(taskDto, val), resultUrl);
		} catch (Exception ex) {
			log.error(ex.getLocalizedMessage(), ex);
			taskService.save(ex, taskDto);
			return errorResponse((Exception) ErrorUtil.getThrowable(ex));
		}
	}

}
