package com.dbs.priviledge.web.controller;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mapping.PropertyReferenceException;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import com.dbs.priviledge.config.Constant;
import com.dbs.priviledge.domain.Authority;
import com.dbs.priviledge.service.AuthorityService;

@PreAuthorize("hasRole('USER_MANAGEMENT')")
@Controller
public class AuthorityController extends AbstractController {

	private final Logger LOG = LoggerFactory.getLogger(AuthorityController.class);

	private final String REDIRECT = "redirect:/authority";
	
	private final String VIEW_TEMPLATE = "authority/view";

	private final String SORT_BY = "name";

	private final AuthorityService authorityService;

	public AuthorityController(AuthorityService authorityService) {
		this.authorityService = authorityService;
	}

	@GetMapping("/authority")
	public String view(HttpServletRequest request, @PageableDefault Pageable pageable) {
		try {
			Page<Authority> page = authorityService.findAll(getKeyword(request), isValid(SORT_BY, pageable));

			if ((page.getNumber() > 0) && (page.getNumber() + 1 > page.getTotalPages())) {
				return REDIRECT;
			} else {
				request.setAttribute(Constant.PAGE, page);
			}

			return VIEW_TEMPLATE;
		} catch (IllegalArgumentException | PropertyReferenceException ex) {
			LOG.error(ex.getLocalizedMessage());
			return REDIRECT;
		}
	}
	
}
