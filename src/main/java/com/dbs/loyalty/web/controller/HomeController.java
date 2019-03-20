package com.dbs.loyalty.web.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import com.dbs.loyalty.exception.NotFoundException;

@Controller
public class HomeController{

	public static final String HOME_URL = "/home";
	
	public static final String VIEW_TEMPLATE = "home/view";
	
	public static final String REDIRECT_HOME = "redirect:/home";
	
	@GetMapping("/")
	public String redirect() {
		return REDIRECT_HOME;
	}
	
	@GetMapping(HOME_URL)
	public String view(HttpServletRequest request) throws NotFoundException {
		return VIEW_TEMPLATE;
	}

}
