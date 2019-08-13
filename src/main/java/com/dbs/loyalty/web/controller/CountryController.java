package com.dbs.loyalty.web.controller;

import java.util.Map;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.dbs.loyalty.config.constant.Constant;
import com.dbs.loyalty.config.constant.DomainConstant;
import com.dbs.loyalty.domain.Country;
import com.dbs.loyalty.service.CountryService;
import com.dbs.loyalty.util.MessageUtil;
import com.dbs.loyalty.util.PageUtil;
import com.dbs.loyalty.util.QueryStringUtil;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Controller
@RequestMapping("/country")
public class CountryController {
	
	private static final String REDIRECT 	= "redirect:/country";
	
	private static final String VIEW 		= "country/country-view";
	
	private static final String DETAIL 		= "country/country-detail";

	private final CountryService countryService;

	@PreAuthorize("hasAnyRole('COUNTRY_MK', 'COUNTRY_CK')")
	@GetMapping
	public String viewPromoCategories(
			@ModelAttribute(Constant.TOAST) String toast, 
			@RequestParam Map<String, String> params, 
			Sort sort, Model model) {
		
		Order order = PageUtil.getOrder(sort, DomainConstant.NAME);
		Page<Country> page = countryService.findAll(params, PageUtil.getPageable(params, order));

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
	
	@PreAuthorize("hasAnyRole('COUNTRY_MK', 'COUNTRY_CK')")
	@GetMapping("/{id}/detail")
	public String viewCountryDetail(ModelMap model, @PathVariable int id){
		getById(model, id);		
		return DETAIL;
	}

	public void getById(ModelMap model, int id){
		Optional<Country> current = countryService.findById(id);
		
		if (current.isPresent()) {
			model.addAttribute(DomainConstant.COUNTRY, current.get());
		} else {
			model.addAttribute(Constant.ERROR, MessageUtil.getNotFoundMessage(String.valueOf(id)));
		}
	}

}
