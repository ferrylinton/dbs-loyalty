package com.dbs.loyalty.web.controller;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.mapping.PropertyReferenceException;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.dbs.loyalty.config.Constant;
import com.dbs.loyalty.domain.LogLogin;
import com.dbs.loyalty.service.LogLoginService;


@Controller
@RequestMapping("/loglogin")
public class LogLoginController extends AbstractPageController {
	
	private final Logger LOG = LoggerFactory.getLogger(LogLoginController.class);

	private final String VIEW_TEMPLATE = "loglogin/view";

	private final String SORT_BY = "createdDate";
	
	private final LogLoginService logLoginService;

	public LogLoginController(LogLoginService logLoginService) {
		this.logLoginService = logLoginService;
	}
	
	@PreAuthorize("hasRole('ADMIN')")
	@GetMapping
	public String view(@RequestParam Map<String,String> params, @PageableDefault(direction = Direction.DESC, sort = "createdDate") Pageable pageable, HttpServletRequest request) {
		Page<LogLogin> page = null;

		try {
			page = logLoginService.findAll(pageable, request);
		} catch (IllegalArgumentException | PropertyReferenceException ex) {
			LOG.error(ex.getMessage());
			page = logLoginService.findAll(PageRequest.of(0, 10, Sort.by(SORT_BY)), request);
		}

		request.setAttribute(Constant.PAGE, page);
		setParamsQueryString(params, request);
		setPagerQueryString(page, request);
		return VIEW_TEMPLATE;
	}

}
