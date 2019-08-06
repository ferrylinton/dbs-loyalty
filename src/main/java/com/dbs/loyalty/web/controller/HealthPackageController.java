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
import com.dbs.loyalty.domain.HealthPackage;
import com.dbs.loyalty.service.HealthPackageService;
import com.dbs.loyalty.util.MessageUtil;
import com.dbs.loyalty.util.PageUtil;
import com.dbs.loyalty.util.QueryStringUtil;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Controller
@RequestMapping("/healthpackage")
public class HealthPackageController {

	private static final String REDIRECT 	= "redirect:/healthpackage";
	
	private static final String VIEW 		= "healthpackage/healthpackage-view";
	
	private static final String DETAIL 		= "healthpackage/healthpackage-detail";

	private final HealthPackageService healthPackageService;

	@PreAuthorize("hasAnyRole('HEALTH_PACKAGE_MK', 'HEALTH_PACKAGE_CK')")
	@GetMapping
	public String viewHealthPackages(
			@ModelAttribute(Constant.TOAST) String toast, 
			@RequestParam Map<String, String> params, 
			Sort sort, Model model) {
		
		Order order = PageUtil.getOrder(sort, DomainConstant.NAME);
		Page<HealthPackage> page = healthPackageService.findAll(params, PageUtil.getPageable(params, order));

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
	
	@PreAuthorize("hasAnyRole('HEALTH_PACKAGE_MK', 'HEALTH_PACKAGE_CK')")
	@GetMapping("/{id}/detail")
	public String viewHealthPackageDetail(ModelMap model, @PathVariable String id){
		getById(model, id);		
		return DETAIL;
	}

	private void getById(ModelMap model, String id){
		Optional<HealthPackage> current = healthPackageService.findById(id);
		
		if (current.isPresent()) {
			model.addAttribute(DomainConstant.HEALTH_PACKAGE, current.get());
		} else {
			model.addAttribute(Constant.ERROR, MessageUtil.getNotFoundMessage(id));
		}
	}
	
}
