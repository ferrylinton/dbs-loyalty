package com.dbs.loyalty.web.controller;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mapping.PropertyReferenceException;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.dbs.loyalty.config.Constant;
import com.dbs.loyalty.domain.LogLogin;
import com.dbs.loyalty.service.LogLoginService;


@Controller
@RequestMapping("/web/logLogin")
public class LogLoginController extends AbstractController {
	
	private final Logger LOG = LoggerFactory.getLogger(LogLoginController.class);

	private final String VIEW_TEMPLATE = "logLogin/view";

	private final String SORT_BY = "createdDate";
	
	private final LogLoginService logLoginService;

	public LogLoginController(LogLoginService logLoginService) {
		this.logLoginService = logLoginService;
	}
	
	@PreAuthorize("hasRole('ADMIN')")
	@GetMapping
	public String view(@PageableDefault Pageable pageable, HttpServletRequest request) {
		Page<LogLogin> page = null;

		try {
			page = logLoginService.findAll(isValid(SORT_BY, pageable), request);
		} catch (IllegalArgumentException | PropertyReferenceException ex) {
			LOG.error(ex.getMessage());
			page = logLoginService.findAll(PageRequest.of(0, 10, Sort.by(SORT_BY)), request);
		}

		request.setAttribute(Constant.PAGE, page);
		return VIEW_TEMPLATE;
	}

}
