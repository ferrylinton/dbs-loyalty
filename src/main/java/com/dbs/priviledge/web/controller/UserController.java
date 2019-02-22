package com.dbs.priviledge.web.controller;

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

import com.dbs.priviledge.config.Constant;
import com.dbs.priviledge.domain.Role;
import com.dbs.priviledge.domain.User;
import com.dbs.priviledge.exception.NotFoundException;
import com.dbs.priviledge.service.RoleService;
import com.dbs.priviledge.service.UserService;
import com.dbs.priviledge.util.ResponseUtil;
import com.dbs.priviledge.web.validator.FileValidator;
import com.dbs.priviledge.web.validator.UserValidator;

@PreAuthorize("hasRole('USER_MANAGEMENT')")
@Controller
public class UserController extends AbstractController{

	private final Logger LOG = LoggerFactory.getLogger(UserController.class);
	
	private final String REDIRECT = "redirect:/user";
	
	private final String LIST_TEMPLATE = "user/view";
	
	private final String FORM_TEMPLATE = "user/form";

	private final String SORT_BY = "email";
	
	private final UserService userService;
	
	private final RoleService roleService;

	public UserController(UserService userService, RoleService roleService) {
		this.userService = userService;
		this.roleService = roleService;
	}

	@GetMapping("/user")
	public String view(HttpServletRequest request, @PageableDefault Pageable pageable) {
		Page<User> page = null;

		try {
			page = userService.findAll(getKeyword(request), isValid(SORT_BY, pageable));
			
			if (page.getNumber() > 0 && page.getNumber() + 1 > page.getTotalPages()) {
				return REDIRECT;
			} else {
				request.setAttribute(Constant.PAGE, page);
			}

		} catch (PropertyReferenceException ex) {
			LOG.error(ex.getLocalizedMessage());
			return REDIRECT;
		}

		return LIST_TEMPLATE;
	}
	
	@GetMapping("/user/{id}")
	public String view(ModelMap model, @PathVariable String id) throws NotFoundException {
		userService.viewForm(model, id);		
		return FORM_TEMPLATE;
	}
	
	@PostMapping("/user")
	@ResponseBody
	public ResponseEntity<?> save(@Valid @ModelAttribute User user, BindingResult result) {
		if (result.hasErrors()) {
			return ResponseUtil.createBadRequestResponse(result);
		} else {
			return userService.save(user);
		}
	}
	
	@DeleteMapping("/user/{id}")
	@ResponseBody
	public ResponseEntity<?> delete(@PathVariable String id) throws NotFoundException {
		return userService.delete(id);
	}
	
	@ModelAttribute("roles")
	public List<Role> getRoles() {
	    return roleService.findAll();
	}
	
	@ModelAttribute(Constant.ENTITY_URL)
	public String getEntityUrl() {
		return userService.getEntityUrl();
	}

	@InitBinder("user")
	protected void initBinder(WebDataBinder binder) {
		binder.addValidators(new UserValidator(userService));
	}
	
	@InitBinder("file")
	protected void initFileBinder(WebDataBinder binder) {
		binder.addValidators(new FileValidator());
	}
	
}
