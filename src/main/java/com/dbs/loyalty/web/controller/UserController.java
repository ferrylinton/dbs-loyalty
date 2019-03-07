package com.dbs.loyalty.web.controller;

import static com.dbs.loyalty.config.Constant.ERROR;
import static com.dbs.loyalty.config.Constant.PAGE;
import static com.dbs.loyalty.config.Constant.ZERO;

import java.util.List;
import java.util.Optional;

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
import org.springframework.web.bind.annotation.RequestMapping;
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

@PreAuthorize("hasRole('USER_MANAGEMENT')")
@Controller
@RequestMapping("/users")
public class UserController extends AbstractController{

	private final Logger LOG 			= LoggerFactory.getLogger(UserController.class);
	
	private final String USER 			= "user";
	
	private final String USERS 			= "users";
	
	private final String REDIRECT 		= "redirect:/users";

	private final String VIEW_TEMPLATE 	= "users/view";

	private final String FORM_TEMPLATE 	= "users/form";

	private final String SORT_BY 		= "email";
	
	private final UserService userService;
	
	private final RoleService roleService;
	
	private final TaskService taskService;

	public UserController(UserService userService, RoleService roleService, TaskService taskService) {
		this.userService = userService;
		this.roleService = roleService;
		this.taskService = taskService;
	}

	@GetMapping
	public String view(HttpServletRequest request, @PageableDefault Pageable pageable) {
		Page<User> page = null;

		try {
			page = userService.findAll(getKeyword(request), isValid(SORT_BY, pageable));
			
			if (page.getNumber() > 0 && page.getNumber() + 1 > page.getTotalPages()) {
				return REDIRECT;
			} else {
				request.setAttribute(PAGE, page);
			}

		} catch (IllegalArgumentException | PropertyReferenceException ex) {
			LOG.error(ex.getLocalizedMessage());
			request.setAttribute(ERROR, ex.getLocalizedMessage());
			request.setAttribute(PAGE, roleService.findAll(getPageable(SORT_BY)));
		}

		return VIEW_TEMPLATE;
	}
	
	@GetMapping("/{id}")
	public String view(ModelMap model, @PathVariable String id) {
		if (id.equals(ZERO)) {
			model.addAttribute(USER, new User());
		} else {
			Optional<User> user = userService.findById(id);
			
			if (user.isPresent()) {
				model.addAttribute(USER, user.get());
			} else {
				model.addAttribute(ERROR, getNotFoundMessage(id));
			}
		}
		
		return FORM_TEMPLATE;
	}
	
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

				return taskIsSavedResponse(User.class.getSimpleName(), user.getEmail(), UrlUtil.getyUrl(USERS));
			}
			
		} catch (Exception ex) {
			LOG.error(ex.getLocalizedMessage(), ex);
			return errorResponse(ex);
		}
	}
	
	@DeleteMapping("/{id}")
	@ResponseBody
	public ResponseEntity<?> delete(@PathVariable String id) throws NotFoundException {
		try {
			Optional<User> user = userService.findWithRoleById(id);
			taskService.saveTaskDelete(user.get());
			return taskIsSavedResponse(User.class.getSimpleName(), user.get().getEmail(), UrlUtil.getyUrl(USERS));
		} catch (Exception ex) {
			LOG.error(ex.getLocalizedMessage(), ex);
			return errorResponse(ex);
		}
	}
	
	@ModelAttribute("roles")
	public List<Role> getRoles() {
	    return roleService.findAll();
	}

	@InitBinder("user")
	protected void initBinder(WebDataBinder binder) {
		binder.addValidators(new UserValidator(userService));
	}
	
}
