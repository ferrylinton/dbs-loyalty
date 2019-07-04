package com.dbs.loyalty.web.controller;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.dbs.loyalty.config.constant.Constant;
import com.dbs.loyalty.config.constant.DomainConstant;
import com.dbs.loyalty.domain.Authority;
import com.dbs.loyalty.domain.Role;
import com.dbs.loyalty.service.AuthorityService;
import com.dbs.loyalty.service.RoleService;
import com.dbs.loyalty.service.TaskService;
import com.dbs.loyalty.util.MessageUtil;
import com.dbs.loyalty.util.PageUtil;
import com.dbs.loyalty.util.QueryStringUtil;
import com.dbs.loyalty.web.validator.RoleValidator;
import com.fasterxml.jackson.core.JsonProcessingException;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Controller
public class RoleController {

	private static final String REDIRECT 	= "redirect:/role";
	
	private static final String VIEW 		= "role/role-view";
	
	private static final String DETAIL 		= "role/role-detail";
	
	private static final String FORM 		= "role/role-form";
	
	private static final String SORT_BY 	= "name";
	
	private final RoleService roleService;

	private final AuthorityService authorityService;
	
	private final TaskService taskService;

	@PreAuthorize("hasAnyRole('ROLE_MK', 'ROLE_CK')")
	@GetMapping("/role")
	public String viewRoles(@ModelAttribute(Constant.TOAST) String toast, @RequestParam Map<String, String> params, Sort sort, Model model) {
		Order order = PageUtil.getOrder(sort, SORT_BY);
		Page<Role> page = roleService.findAll(PageUtil.getPageable(params, order), params);

		if (page.getNumber() > 0 && page.getNumber() + 1 > page.getTotalPages()) {
			return REDIRECT;
		}else {
			model.addAttribute(Constant.PAGE, page);
			model.addAttribute(Constant.ORDER, order);
			model.addAttribute(Constant.PREVIOUS, QueryStringUtil.page(order, page.getNumber() - 1));
			model.addAttribute(Constant.NEXT, QueryStringUtil.page(order, page.getNumber() + 1));
			model.addAttribute(Constant.PARAMS, QueryStringUtil.params(params));
			return VIEW;
		}
	}
	
	@PreAuthorize("hasAnyRole('ROLE_MK', 'ROLE_CK')")
	@GetMapping("/role/{id}/detail")
	public String viewRoleDetail(ModelMap model, @PathVariable String id){
		getById(model, id);		
		return DETAIL;
	}

	@PreAuthorize("hasAnyRole('ROLE_MK')")
	@GetMapping("/role/{id}")
	public String viewRoleForm(ModelMap model, @PathVariable String id){
		if (id.equals(Constant.ZERO)) {
			model.addAttribute(DomainConstant.ROLE, new Role());
		} else {
			getById(model, id);
		}
		
		return FORM;
	}

	@Transactional
	@PreAuthorize("hasRole('ROLE_MK')")
	@PostMapping("/role")
	public String save(@Valid @ModelAttribute(DomainConstant.ROLE) Role role, BindingResult result, RedirectAttributes attributes) throws JsonProcessingException{
		if (result.hasErrors()) {
			return FORM;
		}else {
			if(role.getId() == null) {
				taskService.saveTaskAdd(DomainConstant.ROLE, role);
			}else {
				Optional<Role> current = roleService.findWithAuthoritiesById(role.getId());
				
				if(current.isPresent()) {
					taskService.saveTaskModify(DomainConstant.ROLE, current.get(), role);
					roleService.save(true, role.getId());
				}
			}

			attributes.addFlashAttribute(Constant.TOAST, MessageUtil.taskIsSavedMessage(DomainConstant.ROLE, role.getName()));
			return REDIRECT;
		}
	}

	@Transactional
	@PreAuthorize("hasRole('ROLE_MK')")
	@PostMapping("/role/delete/{id}")
	public String deleteRole(@PathVariable String id, RedirectAttributes attributes) throws JsonProcessingException {
		Optional<Role> current = roleService.findWithAuthoritiesById(id);
		
		if(current.isPresent()) {
			taskService.saveTaskDelete(DomainConstant.ROLE, current.get());
			roleService.save(true, id);
			attributes.addFlashAttribute(Constant.TOAST, MessageUtil.taskIsSavedMessage(DomainConstant.ROLE, current.get().getName()));
		}
		
		return REDIRECT;
	}

	@ModelAttribute(DomainConstant.AUTHORITIES)
	public List<Authority> getAuthorities() {
		return authorityService.findAll();
	}

	@InitBinder(DomainConstant.ROLE)
	protected void initBinder(WebDataBinder binder) {
		binder.addValidators(new RoleValidator(roleService));
	}

	private void getById(ModelMap model, String id){
		Optional<Role> current = roleService.findWithAuthoritiesById(id);
		
		if (current.isPresent()) {
			model.addAttribute(DomainConstant.ROLE, current.get());
		} else {
			model.addAttribute(Constant.ERROR, MessageUtil.getNotFoundMessage(id));
		}
	}
	
}
