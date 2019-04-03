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

	private String redirect 		= "redirect:/user";

	private String viewTemplate 	= "user/view";

	private String formTemplate 	= "user/form";

	private String sortBy 			= "username";
	
	private Order defaultOrder				= Order.asc(sortBy).ignoreCase();
	
	private final UserService userService;
	
	private final RoleService roleService;
	
	private final TaskService taskService;
	
	@PreAuthorize("hasAnyRole('USER_MK', 'USER_CK')")
	@GetMapping
	public String view(@RequestParam Map<String, String> params, Sort sort, HttpServletRequest request) {
		Order order = getOrder(sort);
		Page<UserDto> page = userService.findAll(getPageable(params, order), request);

		if (page.getNumber() > 0 && page.getNumber() + 1 > page.getTotalPages()) {
			return redirect;
		}

		request.setAttribute(PAGE, page);
		setParamsQueryString(params, request);
		setPagerQueryString(order, page.getNumber(), request);
		return viewTemplate;
	}
	
	@PreAuthorize("hasAnyRole('USER_MK', 'USER_CK')")
	@GetMapping("/{id}")
	public String view(ModelMap model, @PathVariable String id) {
		if (id.equals(ZERO)) {
			model.addAttribute(USER, new UserDto());
		} else {
			Optional<UserDto> current = userService.findById(id);
			
			if (current.isPresent()) {
				model.addAttribute(USER, current.get());
			} else {
				model.addAttribute(ERROR, getNotFoundMessage(id));
			}
		}
		
		return formTemplate;
	}
	
	@PreAuthorize("hasRole('USER_MK')")
	@PostMapping
	@ResponseBody
	public ResponseEntity<?> save(@Valid @ModelAttribute UserDto userDto, BindingResult result) {
		try {
			if (result.hasErrors()) {
				return badRequestResponse(result);
			} else {
				
				if(userDto.getId() == null) {
					userDto.setPasswordHash(PasswordUtil.getInstance().encode(userDto.getPasswordPlain()));
					userDto.setPasswordPlain(null);
					taskService.saveTaskAdd(USER, userDto);
				}else {
					Optional<UserDto> current = userService.findWithRoleById(userDto.getId());
					
					if(current.isPresent()) {
						userDto.setPasswordHash(current.get().getPasswordHash());
						taskService.saveTaskModify(USER, current.get(), userDto);
					}else {
						throw new NotFoundException();
					}
				}

				return taskIsSavedResponse(USER,  userDto.getUsername(), UrlUtil.getUrl(USER));
			}
			
		} catch (Exception ex) {
			log.error(ex.getLocalizedMessage(), ex);
			return errorResponse(ex);
		}
	}
	
	@PreAuthorize("hasRole('USER_MK')")
	@DeleteMapping("/{id}")
	@ResponseBody
	public ResponseEntity<?> delete(@PathVariable String id) throws NotFoundException {
		try {
			Optional<UserDto> current = userService.findWithRoleById(id);
			
			if(current.isPresent()) {
				taskService.saveTaskDelete(USER, current.get());
				return taskIsSavedResponse(USER, current.get().getUsername(), UrlUtil.getUrl(USER));
			}else {
				throw new NotFoundException();
			}
			
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
		binder.addValidators(new UserValidator());
	}
	
	private Order getOrder(Sort sort) {
		Order order = sort.getOrderFor(sortBy);
		
		if(order == null) {
			order = defaultOrder;
		}
		
		return order;
	}
	
}
