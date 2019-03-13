package com.dbs.loyalty.web.controller;

import static com.dbs.loyalty.config.Constant.PAGE;

import java.time.Instant;
import java.util.Map;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.dbs.loyalty.domain.Task;
import com.dbs.loyalty.domain.enumeration.TaskStatus;
import com.dbs.loyalty.exception.NotFoundException;
import com.dbs.loyalty.service.TaskService;
import com.dbs.loyalty.util.ErrorUtil;
import com.dbs.loyalty.util.SecurityUtil;
import com.dbs.loyalty.util.UrlUtil;

@Controller
@RequestMapping("/task")
public class TaskController extends AbstractPageController {

	private final Logger LOG 			= LoggerFactory.getLogger(TaskController.class);
	
	private final String ENTITY 		= "task";
	
	private final String REDIRECT 		= "redirect:/task";

	private final String VIEW_TEMPLATE 	= "task/view";

	private final String FORM_TEMPLATE 	= "task/form";

	private final String SORT_BY 		= "madeDate";
	
	private final Order ORDER			= Order.asc(SORT_BY).ignoreCase();

	private final TaskService taskService;

	public TaskController(TaskService taskService) {
		this.taskService = taskService;
	}

	@PreAuthorize("hasAnyRole('TASK', 'TASK_VIEW')")
	@GetMapping
	public String view(@RequestParam Map<String, String> params, Sort sort, HttpServletRequest request) {
		Order order = (sort.getOrderFor(SORT_BY) == null) ? ORDER : sort.getOrderFor(SORT_BY);
		Page<Task> page = taskService.findAll(params, getPageable(params, order), request);
		
		if (page.getNumber() > 0 && page.getNumber() + 1 > page.getTotalPages()) {
			return REDIRECT;
		}
		
		request.setAttribute(PAGE, page);
		setParamsQueryString(params, request);
		setPagerQueryString(order, page.getNumber(), request);
		return VIEW_TEMPLATE;
	}

	@PreAuthorize("hasAnyRole('TASK', 'TASK_VIEW')")
	@GetMapping("/{id}")
	public String view(ModelMap model, @PathVariable String id) throws NotFoundException {
		Optional<Task> task = taskService.findById(id);
		model.addAttribute(ENTITY, task.get());
		return FORM_TEMPLATE;
	}

	@PreAuthorize("hasRole('TASK')")
	@PostMapping
	@ResponseBody
	public ResponseEntity<?> save(@ModelAttribute Task task){
		try {
			task.setTaskStatus(task.getVerified() ? TaskStatus.VERIFIED : TaskStatus.REJECTED);
			task.setChecker(SecurityUtil.getCurrentEmail());
			task.setCheckedDate(Instant.now());
			String val = taskService.save(task);
			return saveResponse(getMessage(task, val), UrlUtil.getyUrl(ENTITY));
		} catch (Exception ex) {
			LOG.error(ex.getLocalizedMessage(), ex);
			taskService.save(ex, task);
			return errorResponse((Exception) ErrorUtil.getThrowable(ex));
		}
	}

}
