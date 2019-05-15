package com.dbs.loyalty.web.controller;

import static com.dbs.loyalty.config.constant.Constant.ERROR;
import static com.dbs.loyalty.config.constant.Constant.PAGE;
import static com.dbs.loyalty.config.constant.Constant.TOAST;
import static com.dbs.loyalty.config.constant.Constant.ZERO;
import static com.dbs.loyalty.config.constant.EntityConstant.AUTHORITIES;
import static com.dbs.loyalty.config.constant.EntityConstant.ROLE;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.dbs.loyalty.domain.Authority;
import com.dbs.loyalty.domain.Role;
import com.dbs.loyalty.service.AuthorityService;
import com.dbs.loyalty.service.RoleService;
import com.dbs.loyalty.service.TaskService;
import com.dbs.loyalty.web.validator.RoleValidator;
import com.fasterxml.jackson.core.JsonProcessingException;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Controller
@RequestMapping("/role")
public class RoleController extends AbstractPageController {

	private static final String REDIRECT = "redirect:/role";
	
	private static final String VIEW = "role/role-view";
	
	private static final String DETAIL = "role/role-detail";
	
	private static final String FORM = "role/role-form";
	
	private static final String SORT_BY = "name";
	
	private final RoleService roleService;

	private final AuthorityService authorityService;
	
	private final TaskService taskService;

	@PreAuthorize("hasAnyRole('ROLE_MK', 'ROLE_CK')")
	@GetMapping
	public String viewRoles(@ModelAttribute(TOAST) String toast, @RequestParam Map<String, String> params, Sort sort, HttpServletRequest request) {
		Order order = getOrder(sort, SORT_BY);
		Page<Role> page = roleService.findAll(getPageable(params, order), request);

		if (page.getNumber() > 0 && page.getNumber() + 1 > page.getTotalPages()) {
			return REDIRECT;
		}else {
			request.setAttribute(TOAST, toast);
			request.setAttribute(PAGE, page);
			setParamsQueryString(params, request);
			setPagerQueryString(order, page.getNumber(), request);
			return VIEW;
		}
	}
	
	@PreAuthorize("hasAnyRole('ROLE_MK', 'ROLE_CK')")
	@GetMapping("/{id}/detail")
	public String viewRoleDetail(ModelMap model, @PathVariable String id){
		getById(model, id);		
		return DETAIL;
	}

	@PreAuthorize("hasAnyRole('ROLE_MK')")
	@GetMapping("/{id}")
	public String viewRoleForm(ModelMap model, @PathVariable String id){
		if (id.equals(ZERO)) {
			model.addAttribute(ROLE, new Role());
		} else {
			getById(model, id);
		}
		
		return FORM;
	}

	@Transactional
	@PreAuthorize("hasRole('ROLE_MK')")
	@PostMapping
	public String save(@Valid @ModelAttribute(ROLE) Role role, BindingResult result, RedirectAttributes attributes) throws JsonProcessingException{
		if (result.hasErrors()) {
			return FORM;
		}else {
			if(role.getId() == null) {
				taskService.saveTaskAdd(ROLE, role);
			}else {
				Optional<Role> current = roleService.findWithAuthoritiesById(role.getId());
				
				if(current.isPresent()) {
					taskService.saveTaskModify(ROLE, current.get(), role);
					roleService.save(true, role.getId());
				}
			}

			attributes.addFlashAttribute(TOAST, taskIsSavedMessage(ROLE, role.getName()));
			return REDIRECT;
		}
	}

	@Transactional
	@PreAuthorize("hasRole('ROLE_MK')")
	@PostMapping("/delete/{id}")
	public String deleteRole(@PathVariable String id, RedirectAttributes attributes) throws JsonProcessingException {
		Optional<Role> current = roleService.findWithAuthoritiesById(id);
		
		if(current.isPresent()) {
			taskService.saveTaskDelete(ROLE, current.get());
			roleService.save(true, id);
			attributes.addFlashAttribute(TOAST, taskIsSavedMessage(ROLE, current.get().getName()));
		}
		
		return REDIRECT;
	}

	@ModelAttribute(AUTHORITIES)
	public List<Authority> getAuthorities() {
		return authorityService.findAll();
	}

	@InitBinder(ROLE)
	protected void initBinder(WebDataBinder binder) {
		binder.addValidators(new RoleValidator(roleService));
	}

	private void getById(ModelMap model, String id){
		Optional<Role> current = roleService.findWithAuthoritiesById(id);
		
		if (current.isPresent()) {
			model.addAttribute(ROLE, current.get());
		} else {
			model.addAttribute(ERROR, getNotFoundMessage(id));
		}
	}
	
}
