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

import com.dbs.loyalty.domain.User;
import com.dbs.loyalty.exception.NotFoundException;
import com.dbs.loyalty.service.RoleService;
import com.dbs.loyalty.service.TaskService;
import com.dbs.loyalty.service.UserService;
import com.dbs.loyalty.service.dto.RoleDto;
import com.dbs.loyalty.service.dto.UserDto;
import com.dbs.loyalty.util.PasswordUtil;
import com.dbs.loyalty.util.UrlUtil;
import com.dbs.loyalty.web.validator.UserValidator;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Controller
@RequestMapping("/user")
public class UserController extends AbstractPageController{

	private String REDIRECT 		= "redirect:/user";

	private String VIEW_TEMPLATE 	= "user/view";

	private String FORM_TEMPLATE 	= "user/form";

	private String SORT_BY 			= "username";
	
	private Order ORDER				= Order.asc(SORT_BY).ignoreCase();
	
	private final UserService userService;
	
	private final RoleService roleService;
	
	private final TaskService taskService;
	
	@PreAuthorize("hasAnyRole('USER_MK', 'USER_CK')")
	@GetMapping
	public String view(@RequestParam Map<String, String> params, Sort sort, HttpServletRequest request) {
		Order order = (sort.getOrderFor(SORT_BY) == null) ? ORDER : sort.getOrderFor(SORT_BY);
		Page<UserDto> page = userService.findAll(getPageable(params, order), request);

		if (page.getNumber() > 0 && page.getNumber() + 1 > page.getTotalPages()) {
			return REDIRECT;
		}

		request.setAttribute(PAGE, page);
		setParamsQueryString(params, request);
		setPagerQueryString(order, page.getNumber(), request);
		return VIEW_TEMPLATE;
	}
	
	@PreAuthorize("hasAnyRole('USER_MK', 'USER_CK')")
	@GetMapping("/{id}")
	public String view(ModelMap model, @PathVariable String id) {
		if (id.equals(ZERO)) {
			model.addAttribute(USER, new User());
		} else {
			Optional<UserDto> user = userService.findById(id);
			
			if (user.isPresent()) {
				model.addAttribute(USER, user.get());
			} else {
				model.addAttribute(ERROR, getNotFoundMessage(id));
			}
		}
		
		return FORM_TEMPLATE;
	}
	
	@PreAuthorize("hasRole('USER_CK')")
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
					taskService.saveTaskAdd(USER, user);
				}else {
					Optional<UserDto> current = userService.findWithRoleById(user.getId());
					user.setPasswordHash(current.get().getPasswordHash());
					taskService.saveTaskModify(USER, current.get(), user);
				}

				return taskIsSavedResponse(USER,  user.getUsername(), UrlUtil.getUrl(USER));
			}
			
		} catch (Exception ex) {
			log.error(ex.getLocalizedMessage(), ex);
			return errorResponse(ex);
		}
	}
	
	@PreAuthorize("hasRole('USER_CK')")
	@DeleteMapping("/{id}")
	@ResponseBody
	public ResponseEntity<?> delete(@PathVariable String id) throws NotFoundException {
		try {
			Optional<UserDto> user = userService.findWithRoleById(id);
			taskService.saveTaskDelete(USER, user.get());
			return taskIsSavedResponse(USER, user.get().getEmail(), UrlUtil.getUrl(USER));
		} catch (Exception ex) {
			log.error(ex.getLocalizedMessage(), ex);
			return errorResponse(ex);
		}
	}
	
	@ModelAttribute("roles")
	public List<RoleDto> getRoles() {
	    return roleService.findAll();
	}

	@InitBinder("userDto")
	protected void initBinder(WebDataBinder binder) {
		binder.addValidators(new UserValidator(userService));
	}
	
}
