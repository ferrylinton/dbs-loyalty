package com.dbs.loyalty.web.controller.task;

import static com.dbs.loyalty.config.constant.Constant.PAGE;
import static com.dbs.loyalty.config.constant.EntityConstant.ROLE;
import static com.dbs.loyalty.config.constant.EntityConstant.TYPE;

import java.util.Map;

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

import com.dbs.loyalty.domain.Task;
import com.dbs.loyalty.service.TaskService;
import com.dbs.loyalty.util.SecurityUtil;
import com.dbs.loyalty.web.response.Response;


@Controller
@RequestMapping("/taskrole")
public class RoleTaskController extends AbstractTaskController {

	private static final String ROLE_CK = "ROLE_CK";
	
	private static final String REDIRECT = "redirect:/taskrole";
	
	public RoleTaskController(final TaskService taskService) {
		super(taskService);
	}

	@PreAuthorize("hasAnyRole('ROLE_MK', 'ROLE_CK')")
	@GetMapping
	public String viewTaskRoles(@RequestParam Map<String, String> params, Sort sort, HttpServletRequest request) {
		Order order = getOrder(sort, MADE_DATE);
		Page<Task> page = taskService.findAll(ROLE, params, getPageable(params, order), request);
		
		if (page.getNumber() > 0 && page.getNumber() + 1 > page.getTotalPages()) {
			return REDIRECT;
		}
		
		request.setAttribute(PAGE, page);
		request.setAttribute(TYPE, ROLE);
		request.setAttribute(IS_CHECKER, SecurityUtil.hasAuthority(ROLE_CK));
		setParamsQueryString(params, request);
		setPagerQueryString(order, page.getNumber(), request);
		return TASK_VIEW_TEMPLATE;
	}
	
	@PreAuthorize("hasAnyRole('ROLE_MK', 'ROLE_CK')")
	@GetMapping("/{id}/detail")
	public String viewTaskRoleDetail(ModelMap model, @PathVariable String id) {
		view(ROLE, model, id);
		return TASK_DETAIL_TEMPLATE;
	}

	@PreAuthorize("hasRole('ROLE_CK')")
	@GetMapping("/{id}")
	public String viewTaskRole(ModelMap model, @PathVariable String id) {
		view(ROLE, model, id);
		return TASK_FORM_TEMPLATE;
	}

	@PreAuthorize("hasRole('ROLE_CK')")
	@PostMapping
	public @ResponseBody ResponseEntity<Response> saveTaskRole(@ModelAttribute Task task){
		return save(task);
	}

}
