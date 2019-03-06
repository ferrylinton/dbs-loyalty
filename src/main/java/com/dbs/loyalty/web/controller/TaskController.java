package com.dbs.loyalty.web.controller;

import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mapping.PropertyReferenceException;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.dbs.loyalty.config.Constant;
import com.dbs.loyalty.domain.Task;
import com.dbs.loyalty.exception.NotFoundException;
import com.dbs.loyalty.model.ErrorResponse;
import com.dbs.loyalty.service.TaskService;
import com.dbs.loyalty.util.ResponseUtil;
import com.fasterxml.jackson.core.JsonProcessingException;

@PreAuthorize("hasAnyRole('TASK', 'USER_MANAGEMENT')")
@Controller
@RequestMapping("/tasks")
public class TaskController extends AbstractController {

	private final Logger LOG = LoggerFactory.getLogger(TaskController.class);

	private final String ENTITY_NAME = "task";
	
	private final String REDIRECT = "redirect:/task";

	private final String VIEW_TEMPLATE = "tasks/view";

	private final String FORM_TEMPLATE = "tasks/form";

	private final String SORT_BY = "madeDate";

	private final TaskService taskService;

	public TaskController(TaskService taskService) {
		this.taskService = taskService;
	}

	@GetMapping
	public String view(HttpServletRequest request, @PageableDefault Pageable pageable) {
		Page<Task> page = null;

		try {
			page = taskService.findAll(isValid(SORT_BY, pageable));
			
			if (page.getNumber() > 0 && page.getNumber() + 1 > page.getTotalPages()) {
				return REDIRECT;
			} else {
				request.setAttribute(Constant.PAGE, page);
			}

			return VIEW_TEMPLATE;
		} catch (PropertyReferenceException ex) {
			LOG.error(ex.getLocalizedMessage());
			return REDIRECT;
		}
	}

	@GetMapping("/{id}")
	public String view(ModelMap model, @PathVariable String id) throws NotFoundException {
		Optional<Task> task = taskService.findById(id);
		model.addAttribute(ENTITY_NAME, task.get());
		return FORM_TEMPLATE;
	}

	@PostMapping
	@ResponseBody
	public ResponseEntity<?> save(@ModelAttribute Task task) throws JsonProcessingException {
		try {
			task = taskService.save(task);
			return ResponseUtil.createSaveResponse(task.getTaskDataType(), ENTITY_NAME);
		} catch (Exception ex) {
			LOG.error(ex.getLocalizedMessage(), ex);
			return ResponseEntity
		            .status(HttpStatus.INTERNAL_SERVER_ERROR)
		            .body(new ErrorResponse(ex.getLocalizedMessage()));
		}
	}
	
}
