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
import com.dbs.loyalty.domain.Setting;
import com.dbs.loyalty.service.CachingService;
import com.dbs.loyalty.service.SettingService;
import com.dbs.loyalty.util.PageUtil;
import com.dbs.loyalty.util.QueryStringUtil;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Controller
@RequestMapping("/setting")
public class SettingController {
	
	private static final String REDIRECT 	= "redirect:/setting";
	
	private static final String VIEW 		= "setting/setting-view";

	private static final String SORT_BY 	= "name";
	
	private final SettingService settingService;
	
	private final CachingService cachingService;

	@PreAuthorize("hasRole('SETTING')")
	@GetMapping
	public String viewSettings(
			@RequestParam Map<String, String> params, 
			Sort sort, Model model) {
		
		Order order = PageUtil.getOrder(sort, SORT_BY);
		Page<Setting> page = settingService.findAll(params, PageUtil.getPageable(params, order));

		if (page.getNumber() > 0 && page.getNumber() + 1 > page.getTotalPages()) {
			return REDIRECT;
		}else {
			evict();
			model.addAttribute(Constant.PAGE, page);
			model.addAttribute(Constant.ORDER, order);
			model.addAttribute(Constant.PREVIOUS, QueryStringUtil.page(order, page.getNumber() - 1));
			model.addAttribute(Constant.NEXT, QueryStringUtil.page(order, page.getNumber() + 1));
			model.addAttribute(Constant.PARAMS, QueryStringUtil.params(params));
			return VIEW;
		}
	}
	
	private void evict() {
		cachingService.evict(CachingConstant.POINT_TO_RUPIAH);
		cachingService.evict(CachingConstant.CURRENCY);
	}
	
}
