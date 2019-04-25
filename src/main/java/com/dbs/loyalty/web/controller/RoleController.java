package com.dbs.loyalty.web.controller;

import static com.dbs.loyalty.config.constant.Constant.ERROR;
import static com.dbs.loyalty.config.constant.Constant.PAGE;
import static com.dbs.loyalty.config.constant.Constant.ZERO;
import static com.dbs.loyalty.config.constant.EntityConstant.ROLE;

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

import com.dbs.loyalty.domain.Authority;
import com.dbs.loyalty.domain.Role;
import com.dbs.loyalty.exception.BadRequestException;
import com.dbs.loyalty.exception.NotFoundException;
import com.dbs.loyalty.service.AuthorityService;
import com.dbs.loyalty.service.RoleService;
import com.dbs.loyalty.service.TaskService;
import com.dbs.loyalty.util.UrlUtil;
import com.dbs.loyalty.web.response.AbstractResponse;
import com.dbs.loyalty.web.validator.RoleValidator;
import com.fasterxml.jackson.core.JsonProcessingException;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Controller
@RequestMapping("/role")
public class RoleController extends AbstractPageController {

	private final RoleService roleService;

	private final AuthorityService authorityService;
	
	private final TaskService taskService;

	@PreAuthorize("hasAnyRole('ROLE_MK', 'ROLE_CK')")
	@GetMapping
	public String viewRoles(@RequestParam Map<String, String> params, Sort sort, HttpServletRequest request) {
		Order order = getOrder(sort, "name");
		Page<Role> page = roleService.findAll(getPageable(params, order), request);

		if (page.getNumber() > 0 && page.getNumber() + 1 > page.getTotalPages()) {
			return "redirect:/role";
		}else {
			request.setAttribute(PAGE, page);
			setParamsQueryString(params, request);
			setPagerQueryString(order, page.getNumber(), request);
			return "role/role-view";
		}
	}
	
	@PreAuthorize("hasAnyRole('ROLE_MK', 'ROLE_CK')")
	@GetMapping("/{id}/detail")
	public String viewRoleDetail(ModelMap model, @PathVariable String id){
		getById(model, id);		
		return "role/role-detail";
	}

	@PreAuthorize("hasAnyRole('ROLE_MK')")
	@GetMapping("/{id}")
	public String viewRoleForm(ModelMap model, @PathVariable String id){
		if (id.equals(ZERO)) {
			model.addAttribute(ROLE, new Role());
		} else {
			getById(model, id);
		}
		
		return "role/role-form";
	}

	@Transactional
	@PreAuthorize("hasRole('ROLE_MK')")
	@PostMapping
	@ResponseBody
	public ResponseEntity<AbstractResponse> save(@ModelAttribute @Valid Role role, BindingResult result) throws JsonProcessingException, NotFoundException, BadRequestException {
		if (result.hasErrors()) {
			throwBadRequestResponse(result);
		}

		if(role.getId() == null) {
			taskService.saveTaskAdd(ROLE, role);
		}else {
			Optional<Role> current = roleService.findWithAuthoritiesById(role.getId());
			
			if(current.isPresent()) {
				taskService.saveTaskModify(ROLE, current.get(), role);
			}else {
				throw new NotFoundException();
			}
		}

		return taskIsSavedResponse(ROLE, role.getName(), UrlUtil.getUrl(ROLE));
	}

	@Transactional
	@PreAuthorize("hasRole('ROLE_MK')")
	@DeleteMapping("/{id}")
	public @ResponseBody ResponseEntity<AbstractResponse> delete(@PathVariable String id) throws NotFoundException, JsonProcessingException{
		Optional<Role> current = roleService.findWithAuthoritiesById(id);
		
		if(current.isPresent()) {
			current.get().setPending(true);
			taskService.saveTaskDelete(ROLE, current.get());
			roleService.save(current.get());
			return taskIsSavedResponse(ROLE, current.get().getName(), UrlUtil.getUrl(ROLE));
		}else {
			throw new NotFoundException();
		}
	}

	@ModelAttribute("authorities")
	public List<Authority> getAuthorities() {
		return authorityService.findAll();
	}

	@InitBinder("role")
	protected void initBinder(WebDataBinder binder) {
		binder.addValidators(new RoleValidator(roleService));
	}

	private void getById(ModelMap model, String id){
		Optional<Role> current = roleService.findWithAuthoritiesById(id);
		
		if (current.isPresent()) {
			model.addAttribute(ROLE, current.get());
		} else {
			model.addAttribute(ERROR, getNotFoundMessage(id));
		}
	}
	
}
