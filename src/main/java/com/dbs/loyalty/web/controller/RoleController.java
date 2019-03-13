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

import com.dbs.loyalty.domain.Authority;
import com.dbs.loyalty.domain.Role;
import com.dbs.loyalty.service.AuthorityService;
import com.dbs.loyalty.service.RoleService;
import com.dbs.loyalty.service.TaskService;
import com.dbs.loyalty.util.UrlUtil;
import com.dbs.loyalty.web.validator.RoleValidator;

@Controller
@RequestMapping("/role")
public class RoleController extends AbstractPageController {

	private final Logger LOG 			= LoggerFactory.getLogger(RoleController.class);

	private final String ENTITY 		= "role";
	
	private final String REDIRECT 		= "redirect:/role";

	private final String VIEW_TEMPLATE 	= "role/view";

	private final String FORM_TEMPLATE 	= "role/form";

	private final String SORT_BY 		= "name";
	
	private final Order ORDER			= Order.asc(SORT_BY).ignoreCase();

	private final RoleService roleService;

	private final AuthorityService authorityService;
	
	private final TaskService taskService;

	public RoleController(RoleService roleService, AuthorityService authorityService, TaskService taskService) {
		this.roleService = roleService;
		this.authorityService = authorityService;
		this.taskService = taskService;
	}

	@PreAuthorize("hasAnyRole('USER', 'USER_VIEW')")
	@GetMapping
	public String view(@RequestParam Map<String, String> params, Sort sort, HttpServletRequest request) {
		Order order = (sort.getOrderFor(SORT_BY) == null) ? ORDER : sort.getOrderFor(SORT_BY);
		Page<Role> page = roleService.findAll(getPageable(params, order), request);

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
	public String view(ModelMap model, @PathVariable String id){
		if (id.equals(ZERO)) {
			model.addAttribute(ENTITY, new Role());
		} else {
			Optional<Role> role = roleService.findById(id);
			System.out.println("----------------- role.isPresent() : " + role.isPresent());
			if (role.isPresent()) {
				model.addAttribute(ENTITY, role.get());
			} else {
				model.addAttribute(ERROR, getNotFoundMessage(id));
			}
		}
		
		return FORM_TEMPLATE;
	}

	@PreAuthorize("hasRole('USER')")
	@PostMapping
	@ResponseBody
	public ResponseEntity<?> save(@ModelAttribute @Valid Role role, BindingResult result) {
		try {
			if (result.hasErrors()) {
				return badRequestResponse(result);
			} else {
				if(role.getId() == null) {
					taskService.saveTaskAdd(role);
				}else {
					Optional<Role> current = roleService.findWithAuthoritiesById(role.getId());
					taskService.saveTaskModify(current.get(), role);
				}

				return taskIsSavedResponse(Role.class.getSimpleName(), role.getName(), UrlUtil.getyUrl(ENTITY));
			}
			
		} catch (Exception ex) {
			LOG.error(ex.getLocalizedMessage(), ex);
			return errorResponse(ex);
		}
	}

	@PreAuthorize("hasRole('USER')")
	@DeleteMapping("/{id}")
	@ResponseBody
	public ResponseEntity<?> delete(@PathVariable String id){
		try {
			Optional<Role> role = roleService.findWithAuthoritiesById(id);
			taskService.saveTaskDelete(role.get());
			return taskIsSavedResponse(Role.class.getSimpleName(), role.get().getName(), UrlUtil.getyUrl(ENTITY));
		} catch (Exception ex) {
			LOG.error(ex.getLocalizedMessage(), ex);
			return errorResponse(ex);
		}
	}

	@ModelAttribute("authorities")
	public List<Authority> getAuthorities() {
		return authorityService.findAll(Sort.by(SORT_BY));
	}

	@InitBinder("role")
	protected void initBinder(WebDataBinder binder) {
		binder.addValidators(new RoleValidator(roleService));
	}

}
