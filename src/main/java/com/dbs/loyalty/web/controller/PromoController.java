package com.dbs.loyalty.web.controller;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
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
import com.dbs.loyalty.domain.Promo;
import com.dbs.loyalty.domain.PromoCategory;
import com.dbs.loyalty.service.PromoCategoryService;
import com.dbs.loyalty.service.PromoService;
import com.dbs.loyalty.util.MessageUtil;
import com.dbs.loyalty.util.PageUtil;
import com.dbs.loyalty.util.QueryStringUtil;
import com.dbs.loyalty.web.customeditor.LocalDateEditor;
import com.dbs.loyalty.web.validator.PromoValidator;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Controller
@RequestMapping("/promo")
public class PromoController {

	private static final String REDIRECT 	= "redirect:/promo";
	
	private static final String VIEW 		= "promo/promo-view";
	
	private static final String DETAIL 		= "promo/promo-detail";
	
	private static final String FORM 		= "promo/promo-form";

	private final PromoService promoService;

	private final PromoCategoryService promoCategoryService;

	@PreAuthorize("hasAnyRole('PROMO_MK', 'PROMO_CK')")
	@GetMapping
	public String viewPromos(
			@ModelAttribute(Constant.TOAST) String toast, 
			@RequestParam Map<String, String> params, 
			Sort sort, Model model) {
		
		Order order = PageUtil.getOrder(sort, DomainConstant.TITLE);
		Page<Promo> page = promoService.findAll(params, PageUtil.getPageable(params, order));

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
	
	@PreAuthorize("hasAnyRole('PROMO_MK', 'PROMO_CK')")
	@GetMapping("/{id}/detail")
	public String viewPromoDetail(ModelMap model, @PathVariable String id){
		getById(model, id);
		return DETAIL;
	}

	@PreAuthorize("hasRole('PROMO_MK')")
	@GetMapping("/{id}")
	public String viewPromoForm(ModelMap model, @PathVariable String id){
		if (id.equals(Constant.ZERO)) {
			model.addAttribute(DomainConstant.PROMO, new Promo());
		} else {
			getById(model, id);
		}
		
		return FORM;
	}

	@Transactional
	@PreAuthorize("hasRole('PROMO_MK')")
	@PostMapping
	public String savePromo(@Valid @ModelAttribute(DomainConstant.PROMO) Promo promo, BindingResult result, RedirectAttributes attributes) {
		if (result.hasErrors()) {
			return FORM;
		}else {
			try {
				promoService.taskSave(promo);
				attributes.addFlashAttribute(Constant.TOAST, MessageUtil.taskIsSaved(DomainConstant.PROMO, promo.getTitle()));
			} catch (Exception e) {
				attributes.addFlashAttribute(Constant.TOAST, e.getLocalizedMessage());
			}

			return REDIRECT;
		}
	}

	@Transactional
	@PreAuthorize("hasRole('PROMO_MK')")
	@PostMapping("/delete/{id}")
	public String deletePromo(@PathVariable String id, RedirectAttributes attributes) throws IOException {
		try {
			Promo promo = promoService.getOne(id);
			promoService.taskDelete(promo);
			attributes.addFlashAttribute(Constant.TOAST, MessageUtil.taskIsSavedMessage(DomainConstant.PROMO, promo.getTitle()));
		} catch (Exception e) {
			attributes.addFlashAttribute(Constant.TOAST, e.getLocalizedMessage());
		}
		
		return REDIRECT;
	}

	@ModelAttribute(DomainConstant.PROMO_CATEGORIES)
	public List<PromoCategory> getPromoCategories() {
		return promoCategoryService.findAll();
	}
	
	@InitBinder(DomainConstant.PROMO)
	protected void initBinder(WebDataBinder binder) {
		binder.registerCustomEditor(LocalDate.class, new LocalDateEditor());
		
		binder.addValidators(new PromoValidator(promoService));
	}

	private void getById(ModelMap model, String id){
		Optional<Promo> current = promoService.findById(id);
		
		if (current.isPresent()) {
			model.addAttribute(DomainConstant.PROMO, current.get());
		} else {
			model.addAttribute(Constant.ERROR, MessageUtil.getNotFoundMessage(id));
		}
	}

}
