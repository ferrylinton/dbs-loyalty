package com.dbs.loyalty.web.controller;

import static com.dbs.loyalty.config.constant.Constant.ERROR;
import static com.dbs.loyalty.config.constant.Constant.PAGE;
import static com.dbs.loyalty.config.constant.Constant.TOAST;
import static com.dbs.loyalty.config.constant.Constant.ZERO;
import static com.dbs.loyalty.config.constant.EntityConstant.PROMO_CATEGORY;

import java.util.Map;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.dbs.loyalty.domain.PromoCategory;
import com.dbs.loyalty.service.PromoCategoryService;
import com.dbs.loyalty.service.TaskService;
import com.dbs.loyalty.web.validator.PromoCategoryValidator;
import com.fasterxml.jackson.core.JsonProcessingException;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Controller
@RequestMapping("/promocategory")
public class PromoCategoryController extends AbstractPageController {
	
	private static final String REDIRECT = "redirect:/promocategory";
	
	private static final String VIEW = "promocategory/promocategory-view";
	
	private static final String DETAIL = "promocategory/promocategory-detail";
	
	private static final String FORM = "promocategory/promocategory-form";
	
	private static final String SORT_BY = "name";
	
	private final PromoCategoryService promoCategoryService;

	private final TaskService taskService;

	@PreAuthorize("hasAnyRole('PROMO_CATEGORY_MK', 'PROMO_CATEGORY_CK')")
	@GetMapping
	public String viewPromoCategories(@ModelAttribute(TOAST) String toast, @RequestParam Map<String, String> params, Sort sort, HttpServletRequest request) {
		Order order = getOrder(sort, SORT_BY);
		Page<PromoCategory> page = promoCategoryService.findAll(getPageable(params, order), request);

		if (page.getNumber() > 0 && page.getNumber() + 1 > page.getTotalPages()) {
			return REDIRECT;
		}else {
			request.setAttribute(PAGE, page);
			setParamsQueryString(params, request);
			setPagerQueryString(order, page.getNumber(), request);
			return VIEW;
		}
	}
	
	@PreAuthorize("hasAnyRole('PROMO_CATEGORY_MK', 'PROMO_CATEGORY_CK')")
	@GetMapping("/{id}/detail")
	public String viewPromoCategoryDetail(ModelMap model, @PathVariable String id){
		getById(model, id);		
		return DETAIL;
	}

	@PreAuthorize("hasAnyRole('PROMO_CATEGORY_MK')")
	@GetMapping("/{id}")
	public String viewPromoCategoryForm(ModelMap model, @PathVariable String id){
		if (id.equals(ZERO)) {
			model.addAttribute(PROMO_CATEGORY, new PromoCategory());
		} else {
			getById(model, id);
		}
		
		return FORM;
	}

	@Transactional
	@PreAuthorize("hasRole('PROMO_CATEGORY_MK')")
	@PostMapping
	public String save(@Valid @ModelAttribute(PROMO_CATEGORY)  PromoCategory promoCategory, BindingResult result, RedirectAttributes attributes) throws JsonProcessingException {
		if (result.hasErrors()) {
			return FORM;
		}else {
			if(promoCategory.getId() == null) {
				taskService.saveTaskAdd(PROMO_CATEGORY, promoCategory);
			} else {
				Optional<PromoCategory> current = promoCategoryService.findById(promoCategory.getId());
				
				if(current.isPresent()) {
					taskService.saveTaskModify(PROMO_CATEGORY, current.get(), promoCategory);
					promoCategoryService.save(true, promoCategory.getId());
				}
			}
			
			attributes.addFlashAttribute(TOAST, taskIsSavedMessage(PROMO_CATEGORY, promoCategory.getName()));
			return REDIRECT;
		}
	}

	@Transactional
	@PreAuthorize("hasRole('PROMO_CATEGORY_MK')")
	@PostMapping("/delete/{id}")
	public String deletePromoCategory(@PathVariable String id, RedirectAttributes attributes) throws JsonProcessingException {
		Optional<PromoCategory> current = promoCategoryService.findById(id);
		
		if(current.isPresent()) {
			taskService.saveTaskDelete(PROMO_CATEGORY, current.get());
			promoCategoryService.save(true, id);
			attributes.addFlashAttribute(TOAST, taskIsSavedMessage(PROMO_CATEGORY, current.get().getName()));
		}
		
		return REDIRECT;
	}

	@InitBinder(PROMO_CATEGORY)
	protected void initBinder(WebDataBinder binder) {
		binder.addValidators(new PromoCategoryValidator(promoCategoryService));
	}
	
	public void getById(ModelMap model, String id){
		Optional<PromoCategory> current = promoCategoryService.findById(id);
		
		if (current.isPresent()) {
			model.addAttribute(PROMO_CATEGORY, current.get());
		} else {
			model.addAttribute(ERROR, getNotFoundMessage(id));
		}
	}

}
