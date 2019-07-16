package com.dbs.loyalty.web.controller;

import java.util.Map;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
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

import com.dbs.loyalty.config.constant.Constant;
import com.dbs.loyalty.config.constant.DomainConstant;
import com.dbs.loyalty.domain.prm.PromoCategory;
import com.dbs.loyalty.service.PromoCategoryService;
import com.dbs.loyalty.service.TaskService;
import com.dbs.loyalty.util.MessageUtil;
import com.dbs.loyalty.util.PageUtil;
import com.dbs.loyalty.util.QueryStringUtil;
import com.dbs.loyalty.web.validator.PromoCategoryValidator;
import com.fasterxml.jackson.core.JsonProcessingException;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Controller
@RequestMapping("/promocategory")
public class PromoCategoryController {
	
	private static final String REDIRECT 	= "redirect:/promocategory";
	
	private static final String VIEW 		= "promocategory/promocategory-view";
	
	private static final String DETAIL 		= "promocategory/promocategory-detail";
	
	private static final String FORM 		= "promocategory/promocategory-form";
	
	private static final String SORT_BY 	= "name";
	
	private final PromoCategoryService promoCategoryService;

	private final TaskService taskService;

	@PreAuthorize("hasAnyRole('PROMO_CATEGORY_MK', 'PROMO_CATEGORY_CK')")
	@GetMapping
	public String viewPromoCategories(
			@ModelAttribute(Constant.TOAST) String toast, 
			@RequestParam Map<String, String> params, 
			Sort sort, Model model) {
		
		Order order = PageUtil.getOrder(sort, SORT_BY);
		Page<PromoCategory> page = promoCategoryService.findAll(params, PageUtil.getPageable(params, order));

		if (page.getNumber() > 0 && page.getNumber() + 1 > page.getTotalPages()) {
			return REDIRECT;
		}else {
			model.addAttribute(Constant.PAGE, page);
			model.addAttribute(Constant.ORDER, order);
			model.addAttribute(Constant.PREVIOUS, QueryStringUtil.page(order, page.getNumber() - 1));
			model.addAttribute(Constant.NEXT, QueryStringUtil.page(order, page.getNumber() + 1));
			model.addAttribute(Constant.PARAMS, QueryStringUtil.params(params));
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
		if (id.equals(Constant.ZERO)) {
			model.addAttribute(DomainConstant.PROMO_CATEGORY, new PromoCategory());
		} else {
			getById(model, id);
		}
		
		return FORM;
	}

	@Transactional
	@PreAuthorize("hasRole('PROMO_CATEGORY_MK')")
	@PostMapping
	public String save(@Valid @ModelAttribute(DomainConstant.PROMO_CATEGORY)  PromoCategory promoCategory, BindingResult result, RedirectAttributes attributes) throws JsonProcessingException {
		if (result.hasErrors()) {
			return FORM;
		}else {
			if(promoCategory.getId() == null) {
				taskService.saveTaskAdd(DomainConstant.PROMO_CATEGORY, promoCategory);
			} else {
				Optional<PromoCategory> current = promoCategoryService.findById(promoCategory.getId());
				
				if(current.isPresent()) {
					taskService.saveTaskModify(DomainConstant.PROMO_CATEGORY, current.get(), promoCategory);
					promoCategoryService.save(true, promoCategory.getId());
				}
			}
			
			attributes.addFlashAttribute(Constant.TOAST, MessageUtil.taskIsSavedMessage(DomainConstant.PROMO_CATEGORY, promoCategory.getName()));
			return REDIRECT;
		}
	}

	@Transactional
	@PreAuthorize("hasRole('PROMO_CATEGORY_MK')")
	@PostMapping("/delete/{id}")
	public String deletePromoCategory(@PathVariable String id, RedirectAttributes attributes) throws JsonProcessingException {
		Optional<PromoCategory> current = promoCategoryService.findById(id);
		
		if(current.isPresent()) {
			taskService.saveTaskDelete(DomainConstant.PROMO_CATEGORY, current.get());
			promoCategoryService.save(true, id);
			attributes.addFlashAttribute(Constant.TOAST, MessageUtil.taskIsSavedMessage(DomainConstant.PROMO_CATEGORY, current.get().getName()));
		}
		
		return REDIRECT;
	}

	@InitBinder(DomainConstant.PROMO_CATEGORY)
	protected void initBinder(WebDataBinder binder) {
		binder.addValidators(new PromoCategoryValidator(promoCategoryService));
	}
	
	public void getById(ModelMap model, String id){
		Optional<PromoCategory> current = promoCategoryService.findById(id);
		
		if (current.isPresent()) {
			model.addAttribute(DomainConstant.PROMO_CATEGORY, current.get());
		} else {
			model.addAttribute(Constant.ERROR, MessageUtil.getNotFoundMessage(id));
		}
	}

}
