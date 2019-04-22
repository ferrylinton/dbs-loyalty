package com.dbs.loyalty.web.controller.task;

import static com.dbs.loyalty.config.constant.Constant.PAGE;
import static com.dbs.loyalty.config.constant.EntityConstant.CUSTOMER;
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
import com.dbs.loyalty.web.response.AbstractResponse;


@Controller
@RequestMapping("/taskcustomer")
public class CustomerTaskController extends AbstractTaskController {

	private static final String CUSTOMER_CK = "CUSTOMER_CK";
	
	private static final String REDIRECT = "redirect:/taskcustomer";
	
	public CustomerTaskController(final TaskService taskService) {
		super(taskService);
	}

	@PreAuthorize("hasAnyRole('CUSTOMER_MK', 'CUSTOMER_CK')")
	@GetMapping
	public String viewTaskCustomers(@RequestParam Map<String, String> params, Sort sort, HttpServletRequest request) {
		Order order = getOrder(sort, MADE_DATE);
		Page<Task> page = taskService.findAll(CUSTOMER, params, getPageable(params, order), request);
		
		if (page.getNumber() > 0 && page.getNumber() + 1 > page.getTotalPages()) {
			return REDIRECT;
		}
		
		request.setAttribute(PAGE, page);
		request.setAttribute(TYPE, CUSTOMER);
		request.setAttribute(IS_CHECKER, SecurityUtil.hasAuthority(CUSTOMER_CK));
		setParamsQueryString(params, request);
		setPagerQueryString(order, page.getNumber(), request);
		return TASK_VIEW_TEMPLATE;
	}
	
	@PreAuthorize("hasAnyRole('CUSTOMER_MK', 'CUSTOMER_CK')")
	@GetMapping("/{id}/detail")
	public String viewTaskCustomerDetail(ModelMap model, @PathVariable String id) {
		view(CUSTOMER, model, id);
		return TASK_DETAIL_TEMPLATE;
	}

	@PreAuthorize("hasRole('CUSTOMER_CK')")
	@GetMapping("/{id}")
	public String viewTaskCustomer(ModelMap model, @PathVariable String id) {
		view(CUSTOMER, model, id);
		return TASK_FORM_TEMPLATE;
	}

	@PreAuthorize("hasRole('CUSTOMER_CK')")
	@PostMapping
	public @ResponseBody ResponseEntity<AbstractResponse> saveTaskCustomer(@ModelAttribute Task task){
		return save(task);
	}

}
