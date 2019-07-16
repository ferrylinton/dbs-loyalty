package com.dbs.loyalty.web.controller;

import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.dbs.loyalty.config.constant.CachingConstant;
import com.dbs.loyalty.config.constant.Constant;
import com.dbs.loyalty.domain.LogApiUrl;
import com.dbs.loyalty.service.CachingService;
import com.dbs.loyalty.service.LogApiUrlService;
import com.dbs.loyalty.util.PageUtil;
import com.dbs.loyalty.util.QueryStringUtil;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Controller
@RequestMapping("/logapiurl")
public class LogApiUrlController {
	
	private static final String REDIRECT 	= "redirect:/logapiurl";
	
	private static final String VIEW 		= "logapiurl/logapiurl-view";

	private static final String SORT_BY 	= "url";
	
	private final LogApiUrlService logApiUrlService;
	
	private final CachingService cachingService;

	@PreAuthorize("hasRole('LOG')")
	@GetMapping
	public String viewLogApiUrls(
			@RequestParam Map<String, String> params, 
			Sort sort, Model model) {
		
		Order order = PageUtil.getOrder(sort, SORT_BY);
		Page<LogApiUrl> page = logApiUrlService.findAll(params, PageUtil.getPageable(params, order));

		if (page.getNumber() > 0 && page.getNumber() + 1 > page.getTotalPages()) {
			return REDIRECT;
		}else {
			cachingService.evict(CachingConstant.LOG_API_URLS);
			model.addAttribute(Constant.PAGE, page);
			model.addAttribute(Constant.ORDER, order);
			model.addAttribute(Constant.PREVIOUS, QueryStringUtil.page(order, page.getNumber() - 1));
			model.addAttribute(Constant.NEXT, QueryStringUtil.page(order, page.getNumber() + 1));
			model.addAttribute(Constant.PARAMS, QueryStringUtil.params(params));
			return VIEW;
		}
	}
	
}
