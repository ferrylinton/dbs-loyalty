package com.dbs.loyalty.web.controller.task;

import static com.dbs.loyalty.config.constant.Constant.PAGE;
import static com.dbs.loyalty.config.constant.EntityConstant.ROLE;
import static com.dbs.loyalty.config.constant.EntityConstant.TYPE;
import static com.dbs.loyalty.config.constant.EntityConstant.USER;

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

import com.dbs.loyalty.service.TaskService;
import com.dbs.loyalty.service.dto.TaskDto;
import com.dbs.loyalty.web.response.AbstractResponse;


@Controller
@RequestMapping("/task/user")
public class UserTaskController extends AbstractTaskController {

	public UserTaskController(final TaskService taskService) {
		super(taskService);
	}

	@PreAuthorize("hasAnyRole('USER_MK', 'USER_CK')")
	@GetMapping
	public String viewTaskUsers(@RequestParam Map<String, String> params, Sort sort, HttpServletRequest request) {
		Order order = getOrder(sort, "madeDate");
		Page<TaskDto> page = taskService.findAll(USER, params, getPageable(params, order), request);
		
		if (page.getNumber() > 0 && page.getNumber() + 1 > page.getTotalPages()) {
			return "redirect:/task/user";
		}
		
		request.setAttribute(PAGE, page);
		request.setAttribute(TYPE, USER);
		setParamsQueryString(params, request);
		setPagerQueryString(order, page.getNumber(), request);
		return "task/user/task-user-view";
	}
	
	@PreAuthorize("hasAnyRole('USER_MK', 'USER_CK')")
	@GetMapping("/{id}/detail")
	public String viewTaskUserDetail(ModelMap model, @PathVariable String id) {
		view(ROLE, model, id);
		return "task/user/task-user-detail";
	}

	@PreAuthorize("hasRole('USER_CK')")
	@GetMapping("/{id}")
	public String viewTaskUser(ModelMap model, @PathVariable String id) {
		view(USER, model, id);
		return "task/user/task-user-form";
	}

	@PreAuthorize("hasRole('USER_CK')")
	@PostMapping
	public @ResponseBody ResponseEntity<AbstractResponse> saveTaskUser(@ModelAttribute TaskDto taskDto){
		return save(taskDto);
	}

}
