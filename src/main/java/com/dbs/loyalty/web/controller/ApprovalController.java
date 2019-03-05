package com.dbs.loyalty.web.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mapping.PropertyReferenceException;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.dbs.loyalty.config.Constant;
import com.dbs.loyalty.domain.Authority;
import com.dbs.loyalty.domain.Role;
import com.dbs.loyalty.domain.enumeration.DataType;
import com.dbs.loyalty.domain.Approval;
import com.dbs.loyalty.exception.NotFoundException;
import com.dbs.loyalty.service.AuthorityService;
import com.dbs.loyalty.service.ApprovalService;
import com.dbs.loyalty.util.ResponseUtil;
import com.fasterxml.jackson.core.JsonProcessingException;

@PreAuthorize("hasAnyRole('APPROVAL', 'USER_MANAGEMENT')")
@Controller
public class ApprovalController extends AbstractController {

	private final Logger LOG = LoggerFactory.getLogger(ApprovalController.class);

	private final String REDIRECT = "redirect:/approval";

	private final String LIST_TEMPLATE = "approval/view";

	private final String FORM_TEMPLATE = "approval/form";

	private final String SORT_BY = "dataType";

	private final ApprovalService approvalService;

	private final AuthorityService authorityService;

	public ApprovalController(ApprovalService approvalService, AuthorityService authorityService) {
		this.approvalService = approvalService;
		this.authorityService = authorityService;
	}

	@GetMapping("/approval")
	public String view(HttpServletRequest request, @PageableDefault Pageable pageable) {
		Page<Approval> page = null;

		try {
			page = approvalService.findAll(isValid(SORT_BY, pageable));
			
			if (page.getNumber() > 0 && page.getNumber() + 1 > page.getTotalPages()) {
				return REDIRECT;
			} else {
				request.setAttribute(Constant.PAGE, page);
			}

			return LIST_TEMPLATE;
		} catch (PropertyReferenceException ex) {
			LOG.error(ex.getLocalizedMessage());
			return REDIRECT;
		}
	}

	@GetMapping("/approval/{id}")
	public String view(ModelMap model, @PathVariable String id) throws NotFoundException {
		approvalService.viewForm(model, id);
		return FORM_TEMPLATE;
	}

	@PostMapping("/approval")
	@ResponseBody
	public ResponseEntity<?> save(@ModelAttribute Approval approval) throws JsonProcessingException {
		return approvalService.save(approval);
	}
}
