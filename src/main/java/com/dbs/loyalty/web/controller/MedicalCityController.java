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
import com.dbs.loyalty.domain.MedicalCity;
import com.dbs.loyalty.service.MedicalCityService;
import com.dbs.loyalty.util.MessageUtil;
import com.dbs.loyalty.util.PageUtil;
import com.dbs.loyalty.util.QueryStringUtil;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Controller
@RequestMapping("/medicalcity")
public class MedicalCityController {

	private static final String REDIRECT 	= "redirect:/medicalcity";
	
	private static final String VIEW 		= "medicalcity/medicalcity-view";
	
	private static final String DETAIL 		= "medicalcity/medicalcity-detail";

	private final MedicalCityService medicalCityService;

	@PreAuthorize("hasAnyRole('MEDICAL_CITY_MK', 'MEDICAL_CITY_CK')")
	@GetMapping
	public String viewMedicalCitys(
			@ModelAttribute(Constant.TOAST) String toast, 
			@RequestParam Map<String, String> params, 
			Sort sort, Model model) {
		
		Order order = PageUtil.getOrder(sort, DomainConstant.NAME);
		Page<MedicalCity> page = medicalCityService.findAll(params, PageUtil.getPageable(params, order));

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
	
	@PreAuthorize("hasAnyRole('MEDICAL_CITY_MK', 'MEDICAL_CITY_CK')")
	@GetMapping("/{id}/detail")
	public String viewMedicalCityDetail(ModelMap model, @PathVariable String id){
		getById(model, id);		
		return DETAIL;
	}

	private void getById(ModelMap model, String id){
		Optional<MedicalCity> current = medicalCityService.findById(id);
		
		if (current.isPresent()) {
			model.addAttribute(DomainConstant.MEDICAL_CITY, current.get());
		} else {
			model.addAttribute(Constant.ERROR, MessageUtil.getNotFoundMessage(id));
		}
	}
	
}
