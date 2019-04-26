package com.dbs.loyalty.web.controller;

import static com.dbs.loyalty.config.constant.Constant.ERROR;
import static com.dbs.loyalty.config.constant.Constant.PAGE;
import static com.dbs.loyalty.config.constant.Constant.ZERO;
import static com.dbs.loyalty.config.constant.EntityConstant.USER;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.dbs.loyalty.domain.Role;
import com.dbs.loyalty.domain.User;
import com.dbs.loyalty.exception.BadRequestException;
import com.dbs.loyalty.exception.NotFoundException;
import com.dbs.loyalty.service.RoleService;
import com.dbs.loyalty.service.TaskService;
import com.dbs.loyalty.service.UserLdapService;
import com.dbs.loyalty.service.UserService;
import com.dbs.loyalty.util.PasswordUtil;
import com.dbs.loyalty.util.UrlUtil;
import com.dbs.loyalty.web.response.AbstractResponse;
import com.dbs.loyalty.web.validator.UserValidator;
import com.fasterxml.jackson.core.JsonProcessingException;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Controller
@RequestMapping("/user")
public class UserController extends AbstractPageController{

	private final UserService userService;
	
	private final UserLdapService userLdapService;
	
	private final RoleService roleService;
	
	private final TaskService taskService;
	
	@PreAuthorize("hasAnyRole('USER_MK', 'USER_CK')")
	@GetMapping
	public String viewUsers(@RequestParam Map<String, String> params, Sort sort, HttpServletRequest request) {
		Order order = getOrder(sort, "username");
		Page<User> page = userService.findAll(getPageable(params, order), request);

		if (page.getNumber() > 0 && page.getNumber() + 1 > page.getTotalPages()) {
			return "redirect:/user";
		}else {
			request.setAttribute(PAGE, page);
			setParamsQueryString(params, request);
			setPagerQueryString(order, page.getNumber(), request);
			return "user/user-view";
		}
	}
	
	@PreAuthorize("hasAnyRole('USER_MK', 'USER_CK')")
	@GetMapping("/{id}/detail")
	public String viewRoleDetail(ModelMap model, @PathVariable String id){
		getById(model, id);		
		return "user/user-detail";
	}
	
	@PreAuthorize("hasRole('USER_MK')")
	@GetMapping("/{id}")
	public String viewUserForm(ModelMap model, @PathVariable String id) {
		if (id.equals(ZERO)) {
			model.addAttribute(USER, new User());
		} else {
			getById(model, id);
		}
		
		return "user/user-form";
	}
	
	@Transactional
	@PreAuthorize("hasRole('USER_MK')")
	@PostMapping
	@ResponseBody
	public ResponseEntity<AbstractResponse> save(@Valid @ModelAttribute User user, BindingResult result) throws BadRequestException, JsonProcessingException, NotFoundException {
		if (result.hasErrors()) {
			throwBadRequestResponse(result);
		}
		
		if(user.getId() == null) {
			user.setPasswordHash(PasswordUtil.encode(user.getPasswordPlain()));
			user.setPasswordPlain(null);
			taskService.saveTaskAdd(USER, user);
		}else {
			Optional<User> current = userService.findWithRoleById(user.getId());
			
			if(current.isPresent()) {
				user.setPasswordHash(current.get().getPasswordHash());
				taskService.saveTaskModify(USER, current.get(), user);
				
				current.get().setPending(true);
				userService.save(current.get());
			}else {
				throw new NotFoundException();
			}
		}

		return taskIsSavedResponse(USER,  user.getUsername(), UrlUtil.getUrl(USER));
	}
	
	@Transactional
	@PreAuthorize("hasRole('USER_MK')")
	@DeleteMapping("/{id}")
	@ResponseBody
	public ResponseEntity<AbstractResponse> delete(@PathVariable String id) throws NotFoundException, JsonProcessingException {
		Optional<User> current = userService.findWithRoleById(id);
		
		if(current.isPresent()) {
			taskService.saveTaskDelete(USER, current.get());
			
			current.get().setPending(true);
			userService.save(current.get());
			return taskIsSavedResponse(USER, current.get().getUsername(), UrlUtil.getUrl(USER));
		}else {
			throw new NotFoundException();
		}
	}
	
	@ModelAttribute("roles")
	public List<Role> getRoles() {
	    return roleService.findAll();
	}

	@InitBinder("user")
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
