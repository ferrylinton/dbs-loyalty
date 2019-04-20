package com.dbs.loyalty.web.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import com.dbs.loyalty.service.LogLoginService;
import com.dbs.loyalty.util.SecurityUtil;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Controller
public class HomeController{

	private final LogLoginService logLoginService;
	
	@GetMapping("/")
	public String redirect() {
		return "redirect:/home";
	}
	
	@GetMapping("/home")
	public String view(HttpServletRequest request) {
		request.setAttribute("user", SecurityUtil.getCurrentUser());
		request.setAttribute("logLogin", logLoginService.getLastLogin());
		return "home/home-view";
	}

}
