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
import org.springframework.data.domain.Sort;
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

import com.dbs.loyalty.domain.Authority;
import com.dbs.loyalty.domain.Role;
import com.dbs.loyalty.service.AuthorityService;
import com.dbs.loyalty.service.RoleService;
import com.dbs.loyalty.service.TaskService;
import com.dbs.loyalty.util.UrlUtil;
import com.dbs.loyalty.web.validator.RoleValidator;

@Controller
@RequestMapping("/roles")
public class RoleController extends AbstractController {

	private final Logger LOG 			= LoggerFactory.getLogger(RoleController.class);

	private final String ROLE 			= "role";
	
	private final String ROLES 			= "roles";
	
	private final String REDIRECT 		= "redirect:/roles";

	private final String VIEW_TEMPLATE 	= "roles/view";

	private final String FORM_TEMPLATE 	= "roles/form";

	private final String SORT_BY 		= "name";

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
	public String view(HttpServletRequest request, @PageableDefault Pageable pageable) {
		Page<Role> page = null;

		try {
			page = roleService.findAll(getKeyword(request), isValid(SORT_BY, pageable));
			
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

	@PreAuthorize("hasAnyRole('USER', 'USER_VIEW')")
	@GetMapping("/{id}")
	public String view(ModelMap model, @PathVariable String id){
		if (id.equals(ZERO)) {
			model.addAttribute(ROLE, new Role());
		} else {
			Optional<Role> role = roleService.findById(id);
			
			if (role.isPresent()) {
				model.addAttribute(ROLE, role.get());
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

				return taskIsSavedResponse(Role.class.getSimpleName(), role.getName(), UrlUtil.getyUrl(ROLES));
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
			return taskIsSavedResponse(Role.class.getSimpleName(), role.get().getName(), UrlUtil.getyUrl(ROLES));
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
