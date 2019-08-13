package com.dbs.loyalty.web.controller;

import java.util.List;
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
import org.springframework.web.bind.annotation.ResponseBody;

import com.dbs.loyalty.config.constant.Constant;
import com.dbs.loyalty.config.constant.DomainConstant;
import com.dbs.loyalty.domain.City;
import com.dbs.loyalty.projection.NameOnly;
import com.dbs.loyalty.service.CityService;
import com.dbs.loyalty.util.MessageUtil;
import com.dbs.loyalty.util.PageUtil;
import com.dbs.loyalty.util.QueryStringUtil;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Controller
@RequestMapping("/city")
public class CityController {

	private static final String REDIRECT 	= "redirect:/city";
	
	private static final String VIEW 		= "city/city-view";
	
	private static final String DETAIL 		= "city/city-detail";

	private final CityService cityService;

	@PreAuthorize("hasAnyRole('CITY_MK', 'CITY_CK')")
	@GetMapping
	public String viewPromoCategories(
			@ModelAttribute(Constant.TOAST) String toast, 
			@RequestParam Map<String, String> params, 
			Sort sort, Model model) {
		
		Order order = PageUtil.getOrder(sort, DomainConstant.NAME);
		Page<City> page = cityService.findAll(params, PageUtil.getPageable(params, order));

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
	
	@PreAuthorize("hasAnyRole('CITY_MK', 'CITY_CK')")
	@GetMapping("/{id}/detail")
	public String viewCityDetail(ModelMap model, @PathVariable int id){
		getById(model, id);		
		return DETAIL;
	}

	public void getById(ModelMap model, int id){
		Optional<City> current = cityService.findWithProvinceById(id);
		
		if (current.isPresent()) {
			model.addAttribute(DomainConstant.CITY, current.get());
		} else {
			model.addAttribute(Constant.ERROR, MessageUtil.getNotFoundMessage(String.valueOf(id)));
		}
	}
	
	@GetMapping("/search/{name}")
	public @ResponseBody List<NameOnly> viewAirports(@PathVariable String name) {
		return cityService.findByName(name);
	}

}
