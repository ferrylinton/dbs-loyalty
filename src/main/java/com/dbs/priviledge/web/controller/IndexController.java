package com.dbs.priviledge.web.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import com.dbs.priviledge.exception.NotFoundException;

@Controller("indexController")
public class IndexController{

	private final String INDEX_TEMPLATE = "index";

	public IndexController() {
	}
	
	@GetMapping("/")
	public String view() {
		return "redirect:/index";
	}
	
	@GetMapping("/index")
	public String view(HttpServletRequest request) throws NotFoundException {
		return INDEX_TEMPLATE;
	}

}
