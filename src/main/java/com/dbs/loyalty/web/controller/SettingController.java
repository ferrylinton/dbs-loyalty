package com.dbs.loyalty.web.controller;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.dbs.loyalty.config.constant.CachingConstant;
import com.dbs.loyalty.service.CachingService;
import com.dbs.loyalty.service.SettingService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Controller
@RequestMapping("/setting")
public class SettingController {
	
	private static final String REDIRECT = "redirect:/setting";
	
	private static final String VIEW = "setting/setting-view";
	
	private final SettingService settingService;
	
	private final CachingService cachingService;

	@PreAuthorize("hasRole('SETTING')")
	@GetMapping
	public String viewSettings(HttpServletRequest request) {
		Map<String, String> settings = settingService.settings();
		request.setAttribute(CachingConstant.SETTINGS, settings);
		return VIEW;
	}
	
	@PreAuthorize("hasRole('SETTING')")
	@GetMapping("/reload")
	public String reloadSettings(HttpServletRequest request) {
		cachingService.evict(CachingConstant.SETTINGS);
		return REDIRECT;
	}
	
}
