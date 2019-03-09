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
import com.dbs.loyalty.domain.PromoCategory;
import com.dbs.loyalty.service.AuthorityService;
import com.dbs.loyalty.service.PromoCategoryService;
import com.dbs.loyalty.service.TaskService;
import com.dbs.loyalty.util.UrlUtil;
import com.dbs.loyalty.web.validator.PromoCategoryValidator;

@Controller
@RequestMapping("/promocategories")
public class PromoCategoryController extends AbstractController {

	private final Logger LOG 				= LoggerFactory.getLogger(PromoCategoryController.class);

	private final String PROMOCATEGORY		= "promocategory";
	
	private final String PROMOCATEGORIES	= "promocategories";
	
	private final String REDIRECT 			= "redirect:/promocategories";

	private final String VIEW_TEMPLATE 		= "promocategories/view";

	private final String FORM_TEMPLATE 		= "promocategories/form";

	private final String SORT_BY 			= "name";

	private final PromoCategoryService promoCategoryService;

	private final AuthorityService authorityService;
	
	private final TaskService taskService;

	public PromoCategoryController(PromoCategoryService promoCategoryService, AuthorityService authorityService, TaskService taskService) {
		this.promoCategoryService = promoCategoryService;
		this.authorityService = authorityService;
		this.taskService = taskService;
	}

	@PreAuthorize("hasAnyRole('PROMO', 'PROMO_VIEW')")
	@GetMapping
	public String view(HttpServletRequest request, @PageableDefault Pageable pageable) {
		Page<PromoCategory> page = null;

		try {
			page = promoCategoryService.findAll(getKeyword(request), isValid(SORT_BY, pageable));
			
			if (page.getNumber() > 0 && page.getNumber() + 1 > page.getTotalPages()) {
				return REDIRECT;
			} else {
				request.setAttribute(PAGE, page);
			}
			
		} catch (IllegalArgumentException | PropertyReferenceException ex) {
			LOG.error(ex.getLocalizedMessage());
			request.setAttribute(ERROR, ex.getLocalizedMessage());
			request.setAttribute(PAGE, promoCategoryService.findAll(getPageable(SORT_BY)));
		}
		
		return VIEW_TEMPLATE;
	}

	@PreAuthorize("hasAnyRole('PROMO', 'PROMO_VIEW')")
	@GetMapping("/{id}")
	public String view(ModelMap model, @PathVariable String id){
		if (id.equals(ZERO)) {
			model.addAttribute(PROMOCATEGORY, new PromoCategory());
		} else {
			Optional<PromoCategory> promoCategory = promoCategoryService.findById(id);
			
			if (promoCategory.isPresent()) {
				model.addAttribute(PROMOCATEGORY, promoCategory.get());
			} else {
				model.addAttribute(ERROR, getNotFoundMessage(id));
			}
		}
		
		return FORM_TEMPLATE;
	}

	@PreAuthorize("hasRole('PROMO')")
	@PostMapping
	@ResponseBody
	public ResponseEntity<?> save(@ModelAttribute @Valid PromoCategory promoCategory, BindingResult result) {
		try {
			if (result.hasErrors()) {
				return badRequestResponse(result);
			} else {
				if(promoCategory.getId() == null) {
					taskService.saveTaskAdd(promoCategory);
				}else {
					Optional<PromoCategory> current = promoCategoryService.findById(promoCategory.getId());
					taskService.saveTaskModify(current.get(), promoCategory);
				}

				return taskIsSavedResponse(PromoCategory.class.getSimpleName(), promoCategory.getName(), UrlUtil.getyUrl(PROMOCATEGORIES));
			}
			
		} catch (Exception ex) {
			LOG.error(ex.getLocalizedMessage(), ex);
			return errorResponse(ex);
		}
	}

	@PreAuthorize("hasRole('PROMO')")
	@DeleteMapping("/{id}")
	@ResponseBody
	public ResponseEntity<?> delete(@PathVariable String id){
		try {
			Optional<PromoCategory> promoCategory = promoCategoryService.findById(id);
			taskService.saveTaskDelete(promoCategory.get());
			return taskIsSavedResponse(PromoCategory.class.getSimpleName(), promoCategory.get().getName(), UrlUtil.getyUrl(PROMOCATEGORIES));
		} catch (Exception ex) {
			LOG.error(ex.getLocalizedMessage(), ex);
			return errorResponse(ex);
		}
	}

	@ModelAttribute("authorities")
	public List<Authority> getAuthorities() {
		return authorityService.findAll(Sort.by(SORT_BY));
	}

	@InitBinder("promoCategory")
	protected void initBinder(WebDataBinder binder) {
		binder.addValidators(new PromoCategoryValidator(promoCategoryService));
	}

}
