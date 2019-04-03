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

	private String redirect 		= "redirect:/role";

	private String viewTemplate 	= "role/view";

	private String formTemplate	= "role/form";

	private String sortBy 			= "name";
	
	private Order defaultOrder				= Order.asc(sortBy).ignoreCase();

	private final RoleService roleService;

	private final AuthorityService authorityService;
	
	private final TaskService taskService;

	@PreAuthorize("hasAnyRole('ROLE_MK', 'ROLE_CK')")
	@GetMapping
	public String view(@RequestParam Map<String, String> params, Sort sort, HttpServletRequest request) {
		Order order = getOrder(sort);
		Page<RoleDto> page = roleService.findAll(getPageable(params, order), request);

		if (page.getNumber() > 0 && page.getNumber() + 1 > page.getTotalPages()) {
			return redirect;
		}
		
		request.setAttribute(PAGE, page);
		setParamsQueryString(params, request);
		setPagerQueryString(order, page.getNumber(), request);
		return viewTemplate;
	}

	@PreAuthorize("hasAnyRole('ROLE_MK', 'ROLE_CK')")
	@GetMapping("/{id}")
	public String view(ModelMap model, @PathVariable String id){
		if (id.equals(ZERO)) {
			model.addAttribute(ROLE, new RoleDto());
		} else {
			Optional<RoleDto> current = roleService.findById(id);
			
			if (current.isPresent()) {
				model.addAttribute(ROLE, current.get());
			} else {
				model.addAttribute(ERROR, getNotFoundMessage(id));
			}
		}
		
		return formTemplate;
	}

	@PreAuthorize("hasRole('ROLE_MK')")
	@PostMapping
	public @ResponseBody ResponseEntity<?> save(@ModelAttribute @Valid RoleDto roleDto, BindingResult result) {
		try {
			if (result.hasErrors()) {
				return badRequestResponse(result);
			} else {
				if(roleDto.getId() == null) {
					taskService.saveTaskAdd(ROLE, roleDto);
				}else {
					Optional<RoleDto> current = roleService.findWithAuthoritiesById(roleDto.getId());
					
					if(current.isPresent()) {
						taskService.saveTaskModify(ROLE, current.get(), roleDto);
					}else {
						throw new NotFoundException();
					}
				}

				return taskIsSavedResponse(ROLE, roleDto.getName(), UrlUtil.getUrl(ROLE));
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
			Optional<RoleDto> current = roleService.findWithAuthoritiesById(id);
			
			if(current.isPresent()) {
				taskService.saveTaskDelete(ROLE, current.get());
				return taskIsSavedResponse(ROLE, current.get().getName(), UrlUtil.getUrl(ROLE));
			}else {
				throw new NotFoundException();
			}
			
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

	private Order getOrder(Sort sort) {
		Order order = sort.getOrderFor(sortBy);
		
		if(order == null) {
			order = defaultOrder;
		}
		
		return order;
	}
	
}
