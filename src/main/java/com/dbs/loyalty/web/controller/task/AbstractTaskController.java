package com.dbs.loyalty.web.controller.task;

import static com.dbs.loyalty.config.constant.Constant.ERROR;
import static com.dbs.loyalty.config.constant.Constant.PAGE;
import static com.dbs.loyalty.config.constant.EntityConstant.TASK;
import static com.dbs.loyalty.config.constant.EntityConstant.TYPE;

import java.util.Map;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.ModelMap;

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

	private String redirect 	= "redirect:/task";

	private String viewTemplate = "task/view";

	private String formTemplate = "task/form";

	private String sortBy 		= "madeDate";
	
	private Order defaultOrder	= Order.asc(sortBy).ignoreCase();

	protected final TaskService taskService;
	
	protected String view(String type, Map<String, String> params, Sort sort, HttpServletRequest request) {
		Order order = (sort.getOrderFor(sortBy) == null) ? defaultOrder : sort.getOrderFor(sortBy);
		Page<TaskDto> page = taskService.findAll(type, params, getPageable(params, order), request);
		
		if (page.getNumber() > 0 && page.getNumber() + 1 > page.getTotalPages()) {
			return redirect;
		}
		
		request.setAttribute(PAGE, page);
		request.setAttribute(TYPE, type);
		setParamsQueryString(params, request);
		setPagerQueryString(order, page.getNumber(), request);
		return viewTemplate;
	}
	
	protected String view(String type, ModelMap model, String id) {
		Optional<TaskDto> task = taskService.findById(id);
		
		if (task.isPresent()) {
			model.addAttribute(TASK, task.get());
		} else {
			model.addAttribute(ERROR, getNotFoundMessage(id));
		}
		
		model.addAttribute(TYPE, type);
		return formTemplate;
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
