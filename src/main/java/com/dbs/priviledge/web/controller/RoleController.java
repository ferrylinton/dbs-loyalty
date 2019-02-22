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
import com.dbs.priviledge.domain.Authority;
import com.dbs.priviledge.domain.Role;
import com.dbs.priviledge.exception.NotFoundException;
import com.dbs.priviledge.service.AuthorityService;
import com.dbs.priviledge.service.RoleService;
import com.dbs.priviledge.util.ResponseUtil;
import com.dbs.priviledge.web.validator.RoleValidator;

@PreAuthorize("hasRole('USER_MANAGEMENT')")
@Controller
public class RoleController extends AbstractController {

	private final Logger LOG = LoggerFactory.getLogger(RoleController.class);

	private final String REDIRECT = "redirect:/role";

	private final String LIST_TEMPLATE = "role/view";

	private final String FORM_TEMPLATE = "role/form";

	private final String SORT_BY = "name";

	private final RoleService roleService;

	private final AuthorityService authorityService;

	public RoleController(RoleService roleService, AuthorityService authorityService) {
		this.roleService = roleService;
		this.authorityService = authorityService;
	}

	@GetMapping("/role")
	public String view(HttpServletRequest request, @PageableDefault Pageable pageable) {
		Page<Role> page = null;

		try {
			page = roleService.findAll(getKeyword(request), isValid(SORT_BY, pageable));
			
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

	@GetMapping("/role/{id}")
	public String view(ModelMap model, @PathVariable String id) throws NotFoundException {
		roleService.viewForm(model, id);
		return FORM_TEMPLATE;
	}

	@PostMapping("/role")
	@ResponseBody
	public ResponseEntity<?> save(@ModelAttribute @Valid Role role, BindingResult result) {
		if (result.hasErrors()) {
			return ResponseUtil.createBadRequestResponse(result);
		} else {
			return roleService.save(role);
		}
	}

	@DeleteMapping("/role/{id}")
	@ResponseBody
	public ResponseEntity<?> delete(@PathVariable String id) throws NotFoundException {
		return roleService.delete(id);
	}

	@ModelAttribute("authorities")
	public List<Authority> getAuthorities() {
		return authorityService.findAll();
	}

	@ModelAttribute(Constant.ENTITY_URL)
	public String getEntityUrl() {
		return roleService.getEntityUrl();
	}

	@InitBinder
	protected void initBinder(WebDataBinder binder) {
		binder.addValidators(new RoleValidator(roleService));
	}

}
