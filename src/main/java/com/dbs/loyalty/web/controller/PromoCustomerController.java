package com.dbs.loyalty.web.controller;

import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.dbs.loyalty.config.constant.Constant;
import com.dbs.loyalty.domain.PromoCustomer;
import com.dbs.loyalty.service.PromoCustomerService;
import com.dbs.loyalty.util.PageUtil;
import com.dbs.loyalty.util.QueryStringUtil;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Controller
@RequestMapping("/promo")
public class PromoCustomerController {

	private static final String REDIRECT 	= "redirect:/promo/%s/customer";
	
	private static final String VIEW 		= "promocustomer/promocustomer-view.html";
	
	private static final String SORT_BY 	= "customer.name";
	
	private final PromoCustomerService promoCustomerService;
	
	@PreAuthorize("hasAnyRole('PROMO_MK', 'PROMO_CK')")
	@GetMapping("/{promoId}/customer")
	public String viewPromoCustomers(@RequestParam Map<String, String> params, @PathVariable String promoId, Sort sort, Model model) {
		Order order = PageUtil.getOrder(sort, SORT_BY);
		Page<PromoCustomer> page = promoCustomerService.findPromoCustomers(promoId, params, PageUtil.getPageable(params, order));

		if (page.getNumber() > 0 && page.getNumber() + 1 > page.getTotalPages()) {
			return String.format(REDIRECT, promoId);
		}else {
			model.addAttribute("promoId", promoId);
			model.addAttribute(Constant.PAGE, page);
			model.addAttribute(Constant.ORDER, order);
			model.addAttribute(Constant.PREVIOUS, QueryStringUtil.page(order, page.getNumber() - 1));
			model.addAttribute(Constant.NEXT, QueryStringUtil.page(order, page.getNumber() + 1));
			model.addAttribute(Constant.PARAMS, QueryStringUtil.params(params));
			return VIEW;
		}
	}
	
}
