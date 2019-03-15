package com.dbs.loyalty.web.controller;

import static com.dbs.loyalty.config.Constant.ERROR;
import static com.dbs.loyalty.config.Constant.PAGE;
import static com.dbs.loyalty.config.Constant.ZERO;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;
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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.dbs.loyalty.domain.Role;
import com.dbs.loyalty.domain.User;
import com.dbs.loyalty.exception.NotFoundException;
import com.dbs.loyalty.service.RoleService;
import com.dbs.loyalty.service.TaskService;
import com.dbs.loyalty.service.UserService;
import com.dbs.loyalty.util.PasswordUtil;
import com.dbs.loyalty.util.UrlUtil;
import com.dbs.loyalty.web.validator.UserValidator;

@Controller
@RequestMapping("/user")
public class UserController extends AbstractPageController{

	private final Logger LOG 			= LoggerFactory.getLogger(UserController.class);

	private final String ENTITY 		= "user";
	
	private final String REDIRECT 		= "redirect:/user";

	private final String VIEW_TEMPLATE 	= "user/view";

	private final String FORM_TEMPLATE 	= "user/form";

	private final String SORT_BY 		= "username";
	
	private final Order ORDER			= Order.asc(SORT_BY).ignoreCase();
	
	private final UserService userService;
	
	private final RoleService roleService;
	
	private final TaskService taskService;

	public UserController(UserService userService, RoleService roleService, TaskService taskService) {
		this.userService = userService;
		this.roleService = roleService;
		this.taskService = taskService;
	}

	
	@PreAuthorize("hasAnyRole('USER', 'USER_VIEW')")
	@GetMapping
	public String view(@RequestParam Map<String, String> params, Sort sort, HttpServletRequest request) {
		Order order = (sort.getOrderFor(SORT_BY) == null) ? ORDER : sort.getOrderFor(SORT_BY);
		Page<User> page = userService.findAll(getPageable(params, order), request);

		if (page.getNumber() > 0 && page.getNumber() + 1 > page.getTotalPages()) {
			return REDIRECT;
		}

		request.setAttribute(PAGE, page);
		setParamsQueryString(params, request);
		setPagerQueryString(order, page.getNumber(), request);
		return VIEW_TEMPLATE;
	}
	
	@PreAuthorize("hasAnyRole('USER', 'USER_VIEW')")
	@GetMapping("/{id}")
	public String view(ModelMap model, @PathVariable String id) {
		if (id.equals(ZERO)) {
			model.addAttribute(ENTITY, new User());
		} else {
			Optional<User> user = userService.findById(id);
			
			if (user.isPresent()) {
				model.addAttribute(ENTITY, user.get());
			} else {
				model.addAttribute(ERROR, getNotFoundMessage(id));
			}
		}
		
		return FORM_TEMPLATE;
	}
	
	@PreAuthorize("hasRole('USER')")
	@PostMapping
	@ResponseBody
	public ResponseEntity<?> save(@Valid @ModelAttribute User user, BindingResult result) {
		try {
			if (result.hasErrors()) {
				return badRequestResponse(result);
			} else {
				
				if(user.getId() == null) {
					user.setPasswordHash(PasswordUtil.getInstance().encode(user.getPasswordPlain()));
					user.setPasswordPlain(null);
					taskService.saveTaskAdd(user);
				}else {
					Optional<User> current = userService.findWithRoleById(user.getId());
					user.setPasswordHash(current.get().getPasswordHash());
					taskService.saveTaskModify(current.get(), user);
				}

				return taskIsSavedResponse(User.class.getSimpleName(), user.getUsername(), UrlUtil.getyUrl(ENTITY));
			}
			
		} catch (Exception ex) {
			LOG.error(ex.getLocalizedMessage(), ex);
			return errorResponse(ex);
		}
	}
	
	@PreAuthorize("hasRole('USER')")
	@DeleteMapping("/{id}")
	@ResponseBody
	public ResponseEntity<?> delete(@PathVariable String id) throws NotFoundException {
		try {
			Optional<User> user = userService.findWithRoleById(id);
			taskService.saveTaskDelete(user.get());
			return taskIsSavedResponse(User.class.getSimpleName(), user.get().getEmail(), UrlUtil.getyUrl(ENTITY));
		} catch (Exception ex) {
			LOG.error(ex.getLocalizedMessage(), ex);
			return errorResponse(ex);
		}
	}
	
	@ModelAttribute("roles")
	public List<Role> getRoles() {
	    return roleService.findAll(Sort.by("name"));
	}

	@InitBinder("user")
	protected void initBinder(WebDataBinder binder) {
		binder.addValidators(new UserValidator(userService));
	}
	
}
