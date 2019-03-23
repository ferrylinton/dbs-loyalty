package com.dbs.loyalty.web.controller;

import static com.dbs.loyalty.config.Constant.ERROR;
import static com.dbs.loyalty.config.Constant.PAGE;
import static com.dbs.loyalty.config.Constant.ZERO;

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

import com.dbs.loyalty.domain.Role;
import com.dbs.loyalty.service.AuthorityService;
import com.dbs.loyalty.service.RoleService;
import com.dbs.loyalty.service.TaskService;
import com.dbs.loyalty.service.dto.AuthorityDto;
import com.dbs.loyalty.service.dto.RoleDto;
import com.dbs.loyalty.util.UrlUtil;
import com.dbs.loyalty.web.validator.RoleValidator;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Controller
@RequestMapping("/role")
public class RoleController extends AbstractPageController {

	private String ENTITY 			= "role";
	
	private String REDIRECT 		= "redirect:/role";

	private String VIEW_TEMPLATE 	= "role/view";

	private String FORM_TEMPLATE	= "role/form";

	private String SORT_BY 			= "name";
	
	private Order ORDER				= Order.asc(SORT_BY).ignoreCase();

	private final RoleService roleService;

	private final AuthorityService authorityService;
	
	private final TaskService taskService;

	@PreAuthorize("hasAnyRole('ROLE_MK', 'ROLE_CK')")
	@GetMapping
	public String view(@RequestParam Map<String, String> params, Sort sort, HttpServletRequest request) {
		Order order = (sort.getOrderFor(SORT_BY) == null) ? ORDER : sort.getOrderFor(SORT_BY);
		Page<RoleDto> page = roleService.findAll(getPageable(params, order), request);

		if (page.getNumber() > 0 && page.getNumber() + 1 > page.getTotalPages()) {
			return REDIRECT;
		}
		
		request.setAttribute(PAGE, page);
		setParamsQueryString(params, request);
		setPagerQueryString(order, page.getNumber(), request);
		return VIEW_TEMPLATE;
	}

	@PreAuthorize("hasAnyRole('ROLE_MK', 'ROLE_CK')")
	@GetMapping("/{id}")
	public String view(ModelMap model, @PathVariable String id){
		if (id.equals(ZERO)) {
			model.addAttribute(ENTITY, new RoleDto());
		} else {
			Optional<RoleDto> roleDto = roleService.findById(id);
			
			if (roleDto.isPresent()) {
				model.addAttribute(ENTITY, roleDto.get());
			} else {
				model.addAttribute(ERROR, getNotFoundMessage(id));
			}
		}
		
		return FORM_TEMPLATE;
	}

	@PreAuthorize("hasRole('ROLE_MK')")
	@PostMapping
	public @ResponseBody ResponseEntity<?> save(@ModelAttribute @Valid RoleDto roleDto, BindingResult result) {
		try {
			if (result.hasErrors()) {
				return badRequestResponse(result);
			} else {
				if(roleDto.getId() == null) {
					taskService.saveTaskAdd(roleDto);
				}else {
					Optional<RoleDto> current = roleService.findWithAuthoritiesById(roleDto.getId());
					taskService.saveTaskModify(current.get(), roleDto);
				}

				return taskIsSavedResponse(Role.class.getSimpleName(), roleDto.getName(), UrlUtil.getUrl(ENTITY));
			}
			
		} catch (Exception ex) {
			log.error(ex.getLocalizedMessage(), ex);
			return errorResponse(ex);
		}
	}

	@PreAuthorize("hasRole('ROLE_MK')")
	@DeleteMapping("/{id}")
	public @ResponseBody ResponseEntity<?> delete(@PathVariable String id){
		try {
			Optional<RoleDto> roleDto = roleService.findWithAuthoritiesById(id);
			taskService.saveTaskDelete(roleDto.get());
			return taskIsSavedResponse(Role.class.getSimpleName(), roleDto.get().getName(), UrlUtil.getUrl(ENTITY));
		} catch (Exception ex) {
			log.error(ex.getLocalizedMessage(), ex);
			return errorResponse(ex);
		}
	}

	@ModelAttribute("authorities")
	public List<AuthorityDto> getAuthorities() {
		return authorityService.findAll();
	}

	@InitBinder("roleDto")
	protected void initBinder(WebDataBinder binder) {
		binder.addValidators(new RoleValidator(roleService));
	}

}
