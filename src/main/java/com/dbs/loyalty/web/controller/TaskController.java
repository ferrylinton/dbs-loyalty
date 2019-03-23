package com.dbs.loyalty.web.controller;

import static com.dbs.loyalty.config.Constant.DTO;
import static com.dbs.loyalty.config.Constant.EMPTY;
import static com.dbs.loyalty.config.Constant.ERROR;
import static com.dbs.loyalty.config.Constant.PAGE;
import static com.dbs.loyalty.config.Constant.TYPE;

import java.time.Instant;
import java.util.Map;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

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

import com.dbs.loyalty.domain.enumeration.TaskStatus;
import com.dbs.loyalty.exception.NotFoundException;
import com.dbs.loyalty.service.TaskService;
import com.dbs.loyalty.service.dto.RoleDto;
import com.dbs.loyalty.service.dto.TaskDto;
import com.dbs.loyalty.util.ErrorUtil;
import com.dbs.loyalty.util.SecurityUtil;
import com.dbs.loyalty.util.UrlUtil;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Controller
@RequestMapping("/task")
public class TaskController extends AbstractPageController {

	private String ENTITY 			= "task";
	
	private String REDIRECT 		= "redirect:/task";

	private String VIEW_TEMPLATE 	= "task/view";

	private String FORM_TEMPLATE 	= "task/form";

	private String SORT_BY 			= "madeDate";
	
	private Order ORDER				= Order.asc(SORT_BY).ignoreCase();

	private final TaskService taskService;

	@PreAuthorize("hasAnyRole('ROLE_MK', 'ROLE_CK')")
	@GetMapping("/role")
	public String viewRoleTask(@RequestParam Map<String, String> params, Sort sort, HttpServletRequest request) {
		return view(RoleDto.class.getSimpleName(), params, sort, request);
	}

	@PreAuthorize("hasAnyRole('ROLE_MK', 'ROLE_CK')")
	@GetMapping("/role/{id}")
	public String viewRoleTask(ModelMap model, @PathVariable String id) throws NotFoundException {
		return view(RoleDto.class.getSimpleName(), model, id);
	}

	@PreAuthorize("hasRole('ROLE_CK')")
	@PostMapping("/role")
	public @ResponseBody ResponseEntity<?> saveRoleTask(@ModelAttribute TaskDto taskDto){
		return save(taskDto);
	}
	
	private String view(String type, Map<String, String> params, Sort sort, HttpServletRequest request) {
		Order order = (sort.getOrderFor(SORT_BY) == null) ? ORDER : sort.getOrderFor(SORT_BY);
		Page<TaskDto> page = taskService.findAll(type, params, getPageable(params, order), request);
		
		if (page.getNumber() > 0 && page.getNumber() + 1 > page.getTotalPages()) {
			return REDIRECT;
		}
		
		request.setAttribute(PAGE, page);
		request.setAttribute(TYPE, getType(type));
		setParamsQueryString(params, request);
		setPagerQueryString(order, page.getNumber(), request);
		return VIEW_TEMPLATE;
	}
	
	private String view(String type, ModelMap model, String id) throws NotFoundException {
		Optional<TaskDto> task = taskService.findById(id);
		
		if (task.isPresent()) {
			model.addAttribute(ENTITY, task.get());
		} else {
			model.addAttribute(ERROR, getNotFoundMessage(id));
		}
		
		model.addAttribute(TYPE, getType(type));
		return FORM_TEMPLATE;
	}
	
	private ResponseEntity<?> save(TaskDto taskDto){
		try {
			taskDto.setTaskStatus(taskDto.isVerified() ? TaskStatus.VERIFIED : TaskStatus.REJECTED);
			taskDto.setChecker(SecurityUtil.getCurrentEmail());
			taskDto.setCheckedDate(Instant.now());
			String val = taskService.save(taskDto);
			return saveResponse(getMessage(taskDto, val), UrlUtil.getTaskUrl(ENTITY, getType(taskDto.getTaskDataType())));
		} catch (Exception ex) {
			log.error(ex.getLocalizedMessage(), ex);
			taskService.save(ex, taskDto);
			return errorResponse((Exception) ErrorUtil.getThrowable(ex));
		}
	}
	
	private String getType(String type) {
		return type.replace(DTO, EMPTY).toLowerCase();
	}

}
