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
import com.dbs.loyalty.domain.LogAuditCustomer;
import com.dbs.loyalty.service.LogAuditCustomerService;
import com.dbs.loyalty.util.MessageUtil;
import com.dbs.loyalty.util.PageUtil;
import com.dbs.loyalty.util.QueryStringUtil;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Controller
@RequestMapping("/logauditcustomer")
public class LogAuditCustomerController {

	private static final String REDIRECT 	= "redirect:/logauditcustomer";
	
	private static final String VIEW 		= "logauditcustomer/logauditcustomer-view";
	
	private static final String DETAIL 		= "logauditcustomer/logauditcustomer-detail";
	
	private final LogAuditCustomerService logAuditCustomerService;

	@PreAuthorize("hasRole('LOG')")
	@GetMapping
	public String viewLogAuditCustomers(
			@ModelAttribute(Constant.TOAST) String toast, 
			@RequestParam Map<String, String> params, 
			Sort sort, Model model) {
		
		Order order = PageUtil.getOrderDesc(sort, DomainConstant.CREATED_DATE);
		Page<LogAuditCustomer> page = logAuditCustomerService.findAll(params, PageUtil.getPageable(params, order));
		
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
	
	@PreAuthorize("hasRole('LOG')")
	@GetMapping("/{id}/detail")
	public String LogAuditCustomerDetail(ModelMap model, @PathVariable String id){
		Optional<LogAuditCustomer> logAuditCustomer = logAuditCustomerService.findWithCustomerById(id);
		
		if (logAuditCustomer.isPresent()) {
			model.addAttribute(DomainConstant.LOG_AUDIT_CUSTOMER, logAuditCustomer.get());
		} else {
			model.addAttribute(Constant.ERROR, MessageUtil.getNotFoundMessage(id));
		}
		
		return DETAIL;
	}

}
