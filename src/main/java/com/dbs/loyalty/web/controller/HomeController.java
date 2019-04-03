package com.dbs.loyalty.web.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController{

	private String viewTemplate = "home/view";
	
	private String redirect = "redirect:/home";
	
	@GetMapping("/")
	public String redirect() {
		return redirect;
	}
	
	@GetMapping("/home")
	public String view(HttpServletRequest request) {
		return viewTemplate;
	}

}
