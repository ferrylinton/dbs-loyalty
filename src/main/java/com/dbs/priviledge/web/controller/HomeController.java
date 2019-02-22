package com.dbs.priviledge.web.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import com.dbs.priviledge.exception.NotFoundException;

@Controller
public class HomeController{

	private final String HOME_VIEW_TEMPLATE = "home/view";
	
	private final String REDIRECT_LOGIN = "redirect:/home";
	
	@GetMapping("/")
	public String view() {
		return REDIRECT_LOGIN;
	}
	
	@GetMapping("/home")
	public String view(HttpServletRequest request) throws NotFoundException {
		return HOME_VIEW_TEMPLATE;
	}

}
