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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.dbs.loyalty.config.constant.Constant;
import com.dbs.loyalty.config.constant.DomainConstant;
import com.dbs.loyalty.config.constant.UserTypeConstant;
import com.dbs.loyalty.domain.Role;
import com.dbs.loyalty.domain.User;
import com.dbs.loyalty.service.RoleService;
import com.dbs.loyalty.service.UserLdapService;
import com.dbs.loyalty.service.UserService;
import com.dbs.loyalty.util.MessageUtil;
import com.dbs.loyalty.util.PageUtil;
import com.dbs.loyalty.util.QueryStringUtil;
import com.dbs.loyalty.web.validator.UserValidator;
import com.fasterxml.jackson.core.JsonProcessingException;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Controller
@RequestMapping("/user")
public class UserController{
	
	private static final String REDIRECT 	= "redirect:/user";
	
	private static final String VIEW 		= "user/user-view";
	
	private static final String DETAIL 		= "user/user-detail";
	
	private static final String FORM 		= "user/user-form";

	private final UserService userService;
	
	private final UserLdapService userLdapService;
	
	private final RoleService roleService;

	@PreAuthorize("hasAnyRole('USER_MK', 'USER_CK')")
	@GetMapping
	public String viewUsers(
			@ModelAttribute(Constant.TOAST) String toast, 
			@RequestParam Map<String, String> params, 
			Sort sort, Model model) {
		
		Order order = PageUtil.getOrder(sort, DomainConstant.USERNAME);
		Page<User> page = userService.findAll(params, PageUtil.getPageable(params, order));

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
	
	@PreAuthorize("hasAnyRole('USER_MK', 'USER_CK')")
	@GetMapping("/{id}/detail")
	public String viewUserDetail(ModelMap model, @PathVariable String id){
		getById(model, id);		
		return DETAIL;
	}
	
	@PreAuthorize("hasRole('USER_MK')")
	@GetMapping("/{id}")
	public String viewUserForm(ModelMap model, @PathVariable String id) {
		if (id.equals(Constant.ZERO)) {
			User user = new User();
			user.setUserType(UserTypeConstant.INTERNAL);
			model.addAttribute(DomainConstant.USER, user);
		} else {
			getById(model, id);
		}
		
		return FORM;
	}
	
	@Transactional
	@PreAuthorize("hasRole('USER_MK')")
	@PostMapping
	public String save(@Valid @ModelAttribute(DomainConstant.USER) User user, BindingResult result, RedirectAttributes attributes) throws JsonProcessingException {
		if (result.hasErrors()) {
			return FORM;
		}else {
			try {
				userService.taskSave(user);
				attributes.addFlashAttribute(Constant.TOAST, MessageUtil.taskIsSaved(DomainConstant.USER, user.getUsername()));
			} catch (Exception e) {
				attributes.addFlashAttribute(Constant.TOAST, e.getLocalizedMessage());
			}

			return REDIRECT;
		}
	}
	
	@Transactional
	@PreAuthorize("hasRole('USER_MK')")
	@PostMapping("/delete/{id}")
	public String deleteUser(@PathVariable String id, RedirectAttributes attributes) throws JsonProcessingException {
		try {
			User user = userService.getOne(id);
			userService.taskDelete(user);
			attributes.addFlashAttribute(Constant.TOAST, MessageUtil.taskIsSavedMessage(DomainConstant.USER, user.getUsername()));
		} catch (Exception e) {
			attributes.addFlashAttribute(Constant.TOAST, e.getLocalizedMessage());
		}
		
		return REDIRECT;
	}
	
	@ModelAttribute(DomainConstant.ROLES)
	public List<Role> getRoles() {
	    return roleService.findAll();
	}

	@InitBinder(DomainConstant.USER)
	protected void initBinder(WebDataBinder binder) {
		binder.addValidators(new UserValidator(userService, userLdapService));
	}

	public void getById(ModelMap model, String id) {
		if (id.equals(Constant.ZERO)) {
			model.addAttribute(DomainConstant.USER, new User());
		} else {
			Optional<User> current = userService.findById(id);
			
			if (current.isPresent()) {
				model.addAttribute(DomainConstant.USER, current.get());
			} else {
				model.addAttribute(Constant.ERROR, MessageUtil.getNotFoundMessage(id));
			}
		}
	}
	
}
