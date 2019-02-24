package com.dbs.priviledge.web.controller;

import javax.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.dbs.priviledge.exception.NotFoundException;
import com.dbs.priviledge.model.Password;
import com.dbs.priviledge.service.PasswordService;
import com.dbs.priviledge.util.ResponseUtil;
import com.dbs.priviledge.util.SecurityUtil;


@Controller
public class PasswordController extends AbstractController{

	private final String PASSWORD_TEMPLATE	= "password/view";
	
	private final String ENTITY_NAME = "password";
	
	private final PasswordService passwordService;

	public PasswordController(PasswordService passwordService) {
		this.passwordService = passwordService;
	}

	@PreAuthorize("isAuthenticated()")
	@GetMapping("/password")
	public String viewPassword(ModelMap model) throws NotFoundException {
		passwordService.viewPassword(model, SecurityUtil.getCurrentEmail());
		return PASSWORD_TEMPLATE;
	}
	
	@PreAuthorize("hasRole('USER_MANAGEMENT')")
	@GetMapping("/password/{email}")
	public String viewPassword(ModelMap model, @PathVariable String email) throws NotFoundException {
		passwordService.viewPassword(model, email);
		return PASSWORD_TEMPLATE;
	}

	@PreAuthorize("authenticated")
	@PostMapping("/password")
	@ResponseBody
	public ResponseEntity<?> save(@Valid @ModelAttribute(ENTITY_NAME) Password password, BindingResult result) throws NotFoundException {
		if (result.hasErrors()) {
			return ResponseUtil.createBadRequestResponse(result);
		} else {
			return passwordService.save(password);
		}
	}
	
}
