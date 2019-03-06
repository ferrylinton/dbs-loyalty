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
import org.springframework.http.HttpStatus;
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
import com.dbs.loyalty.domain.enumeration.TaskOperation;
import com.dbs.loyalty.exception.NotFoundException;
import com.dbs.loyalty.model.ErrorResponse;
import com.dbs.loyalty.service.AuthorityService;
import com.dbs.loyalty.service.RoleService;
import com.dbs.loyalty.service.TaskService;
import com.dbs.loyalty.util.ResponseUtil;
import com.dbs.loyalty.util.UrlUtil;
import com.dbs.loyalty.web.validator.RoleValidator;

@PreAuthorize("hasRole('USER_MANAGEMENT')")
@Controller
@RequestMapping("/roles")
public class RoleController extends AbstractController {

	private final Logger log 			= LoggerFactory.getLogger(RoleController.class);

	private final String TASK_DATA_TYPE = "Role";
	
	private final String ROLE 			= "role";
	
	private final String ROLES 			= "roles";
	
	private final String REDIRECT 		= "redirect:/roles";

	private final String VIEW_TEMPLATE 	= "role/view";

	private final String FORM_TEMPLATE 	= "role/form";

	private final String SORT_BY 		= "name";

	private final RoleService roleService;

	private final AuthorityService authorityService;
	
	private final TaskService taskService;

	public RoleController(RoleService roleService, AuthorityService authorityService, TaskService taskService) {
		this.roleService = roleService;
		this.authorityService = authorityService;
		this.taskService = taskService;
	}

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
			
		} catch (PropertyReferenceException ex) {
			log.error(ex.getLocalizedMessage());
			request.setAttribute(ERROR, ex.getLocalizedMessage());
			request.setAttribute(PAGE, roleService.findAll(null, getPageable(SORT_BY)));
		}
		
		return VIEW_TEMPLATE;
	}

	@GetMapping("/{id}")
	public String view(ModelMap model, @PathVariable String id) throws NotFoundException {
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

	@PostMapping
	@ResponseBody
	public ResponseEntity<?> save(@ModelAttribute @Valid Role role, BindingResult result) {
		try {
			if (result.hasErrors()) {
				return badRequestResponse(result);
			} else {
				TaskOperation taskOperation = (role.getId() == null) ? TaskOperation.ADD : TaskOperation.MODIFY;
				taskService.save(taskOperation, TASK_DATA_TYPE, role);
				return taskIsSavedResponse(TASK_DATA_TYPE, role.getName(), UrlUtil.getyUrl(ROLES));
			}
			
		} catch (Exception ex) {
			log.error(ex.getLocalizedMessage(), ex);
			return errorResponse(ex);
		}
	}

	@DeleteMapping("/{id}")
	@ResponseBody
	public ResponseEntity<?> delete(@PathVariable String id) throws NotFoundException {
		try {
			Optional<Role> role = roleService.findById(id);
			roleService.delete(role.get());
			return ResponseUtil.createDeleteResponse(role.get().getName(), ROLE);
		} catch (Exception ex) {
			log.error(ex.getLocalizedMessage(), ex);
			return ResponseEntity
		            .status(HttpStatus.INTERNAL_SERVER_ERROR)
		            .body(new ErrorResponse(ex.getLocalizedMessage()));
		}
	}

	@ModelAttribute("authorities")
	public List<Authority> getAuthorities() {
		return authorityService.findAll();
	}

	@InitBinder
	protected void initBinder(WebDataBinder binder) {
		binder.addValidators(new RoleValidator(roleService));
	}

}
