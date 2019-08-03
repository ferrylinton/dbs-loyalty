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
import com.dbs.loyalty.domain.PriviledgeProduct;
import com.dbs.loyalty.service.PriviledgeProductService;
import com.dbs.loyalty.util.MessageUtil;
import com.dbs.loyalty.util.PageUtil;
import com.dbs.loyalty.util.QueryStringUtil;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Controller
@RequestMapping("/priviledgeproduct")
public class PriviledgeProductController {

	private static final String REDIRECT 	= "redirect:/priviledgeproduct";
	
	private static final String VIEW 		= "priviledgeproduct/priviledgeproduct-view";
	
	private static final String DETAIL 		= "priviledgeproduct/priviledgeproduct-detail";

	private static final String SORT_BY 	= "name";
	
	private final PriviledgeProductService priviledgeProductService;

	@PreAuthorize("hasAnyRole('PRODUCT_MK', 'PRODUCT_CK')")
	@GetMapping
	public String viewPriviledgeProducts(
			@ModelAttribute(Constant.TOAST) String toast, 
			@RequestParam Map<String, String> params, 
			Sort sort, Model model) {
		
		Order order = PageUtil.getOrder(sort, SORT_BY);
		Page<PriviledgeProduct> page = priviledgeProductService.findAll(params, PageUtil.getPageable(params, order));

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
	
	@PreAuthorize("hasAnyRole('PRODUCT_MK', 'PRODUCT_CK')")
	@GetMapping("/{id}/detail")
	public String viewPriviledgeProductDetail(ModelMap model, @PathVariable String id){
		getById(model, id);		
		return DETAIL;
	}

	private void getById(ModelMap model, String id){
		Optional<PriviledgeProduct> current = priviledgeProductService.findById(id);
		
		if (current.isPresent()) {
			model.addAttribute(DomainConstant.PRIVILEDGE_PRODUCT, current.get());
		} else {
			model.addAttribute(Constant.ERROR, MessageUtil.getNotFoundMessage(id));
		}
	}
	
}
