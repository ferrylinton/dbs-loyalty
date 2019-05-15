package com.dbs.loyalty.web.controller;

import static com.dbs.loyalty.config.constant.Constant.ERROR;
import static com.dbs.loyalty.config.constant.Constant.PAGE;
import static com.dbs.loyalty.config.constant.Constant.TOAST;
import static com.dbs.loyalty.config.constant.Constant.ZERO;
import static com.dbs.loyalty.config.constant.EntityConstant.ROLES;
import static com.dbs.loyalty.config.constant.EntityConstant.USER;

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

import com.dbs.loyalty.config.constant.UserTypeConstant;
import com.dbs.loyalty.domain.Role;
import com.dbs.loyalty.domain.User;
import com.dbs.loyalty.service.RoleService;
import com.dbs.loyalty.service.TaskService;
import com.dbs.loyalty.service.UserLdapService;
import com.dbs.loyalty.service.UserService;
import com.dbs.loyalty.util.PasswordUtil;
import com.dbs.loyalty.web.validator.UserValidator;
import com.fasterxml.jackson.core.JsonProcessingException;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Controller
@RequestMapping("/user")
public class UserController extends AbstractPageController{
	
	private static final String REDIRECT = "redirect:/user";
	
	private static final String VIEW = "user/user-view";
	
	private static final String DETAIL = "user/user-detail";
	
	private static final String FORM = "user/user-form";
	
	private static final String SORT_BY = "username";

	private final UserService userService;
	
	private final UserLdapService userLdapService;
	
	private final RoleService roleService;
	
	private final TaskService taskService;
	
	@PreAuthorize("hasAnyRole('USER_MK', 'USER_CK')")
	@GetMapping
	public String viewUsers(@RequestParam Map<String, String> params, Sort sort, HttpServletRequest request) {
		Order order = getOrder(sort, SORT_BY);
		Page<User> page = userService.findAll(getPageable(params, order), request);

		if (page.getNumber() > 0 && page.getNumber() + 1 > page.getTotalPages()) {
			return REDIRECT;
		}else {
			request.setAttribute(PAGE, page);
			setParamsQueryString(params, request);
			setPagerQueryString(order, page.getNumber(), request);
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
		if (id.equals(ZERO)) {
			User user = new User();
			user.setUserType(UserTypeConstant.INTERNAL);
			model.addAttribute(USER, user);
		} else {
			getById(model, id);
		}
		
		return FORM;
	}
	
	@Transactional
	@PreAuthorize("hasRole('USER_MK')")
	@PostMapping
	public String save(@Valid @ModelAttribute(USER) User user, BindingResult result, RedirectAttributes attributes) throws JsonProcessingException {
		if (result.hasErrors()) {
			return FORM;
		}else {
			if(user.getId() == null) {
				if(user.getUserType().equals(UserTypeConstant.EXTERNAL)) {
					user.setPasswordHash(PasswordUtil.encode(user.getPasswordPlain()));
					user.setPasswordPlain(null);
				}
				
				taskService.saveTaskAdd(USER, user);
			}else {
				Optional<User> current = userService.findWithRoleById(user.getId());
				
				if(current.isPresent()) {
					if(user.getUserType().equals(UserTypeConstant.EXTERNAL)) {
						user.setPasswordHash(current.get().getPasswordHash());
					}
					
					taskService.saveTaskModify(USER, current.get(), user);
					userService.save(true, user.getId());
				}
			}

			attributes.addFlashAttribute(TOAST, taskIsSavedMessage(USER, user.getUsername()));
			return REDIRECT;
		}
	}
	
	@Transactional
	@PreAuthorize("hasRole('USER_MK')")
	@PostMapping("/delete/{id}")
	public String deleteUser(@PathVariable String id, RedirectAttributes attributes) throws JsonProcessingException {
		Optional<User> current = userService.findWithRoleById(id);
		
		if(current.isPresent()) {
			taskService.saveTaskDelete(USER, current.get());
			userService.save(true, id);
			attributes.addFlashAttribute(TOAST, taskIsSavedMessage(USER, current.get().getUsername()));
		}
		
		return REDIRECT;
	}
	
	@ModelAttribute(ROLES)
	public List<Role> getRoles() {
	    return roleService.findAll();
	}

	@InitBinder(USER)
	protected void initBinder(WebDataBinder binder) {
		binder.addValidators(new UserValidator(userService, userLdapService));
	}

	public void getById(ModelMap model, String id) {
		if (id.equals(ZERO)) {
			model.addAttribute(USER, new User());
		} else {
			Optional<User> current = userService.findById(id);
			
			if (current.isPresent()) {
				model.addAttribute(USER, current.get());
			} else {
				model.addAttribute(ERROR, getNotFoundMessage(id));
			}
		}
	}
	
}
