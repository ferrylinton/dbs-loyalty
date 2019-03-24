package com.dbs.loyalty.web.controller;

import static com.dbs.loyalty.config.constant.Constant.PAGE;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.dbs.loyalty.domain.LogLogin;
import com.dbs.loyalty.service.LogLoginService;

@Controller
@RequestMapping("/loglogin")
public class LogLoginController extends AbstractPageController {

	private final String REDIRECT		= "redirect:/loglogin";
	
	private final String VIEW_TEMPLATE 	= "loglogin/view";

	private final String SORT_BY 		= "createdDate";
	
	private final Order ORDER			= Order.desc(SORT_BY).ignoreCase();

	private final LogLoginService logLoginService;

	public LogLoginController(LogLoginService logLoginService) {
		this.logLoginService = logLoginService;
	}

	@PreAuthorize("hasRole('ADMIN')")
	@GetMapping
	public String view(@RequestParam Map<String, String> params, Sort sort, HttpServletRequest request) {
		Order order = (sort.getOrderFor(SORT_BY) == null) ? ORDER : sort.getOrderFor(SORT_BY);
		Page<LogLogin> page = logLoginService.findAll(getPageable(params, order), request);
		
		if (page.getNumber() > 0 && page.getNumber() + 1 > page.getTotalPages()) {
			return REDIRECT;
		}

		request.setAttribute(PAGE, page);
		setParamsQueryString(params, request);
		setPagerQueryString(order, page.getNumber(), request);
		return VIEW_TEMPLATE;
	}

}
