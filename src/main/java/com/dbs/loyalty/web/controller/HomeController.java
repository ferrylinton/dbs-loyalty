package com.dbs.loyalty.web.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController{

	@GetMapping("/")
	public String redirect() {
		return "redirect:/home";
	}
	
	@GetMapping("/home")
	public String view(HttpServletRequest request) {
		return "home/view";
	}

}
