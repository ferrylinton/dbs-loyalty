package com.dbs.loyalty.service;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Service;
import org.springframework.web.servlet.LocaleResolver;
import org.thymeleaf.context.WebContext;
import org.thymeleaf.spring5.SpringTemplateEngine;

@Service
public class TemplateService {
	
	private final Set<String> templateSelectors = new HashSet<>(Arrays.asList("content"));
	
	private final SpringTemplateEngine templateEngine;
	
	private final LocaleResolver localeResolver;
	
	private final HttpServletResponse response;
	
	public TemplateService(SpringTemplateEngine templateEngine, LocaleResolver localeResolver, HttpServletResponse response) {
		this.templateEngine = templateEngine;
		this.localeResolver = localeResolver;
		this.response = response;
	}
	
	public String process(String template, HttpServletRequest request) {
		return templateEngine.process(template, templateSelectors, createContext(template, request));
	}
	
	public String process(String template, Map<String, Object> variables, HttpServletRequest request) {
		return templateEngine.process(template, templateSelectors, createContext(template, variables, request));
	}
	
	private WebContext createContext(String template, HttpServletRequest request) {
		return new WebContext(request, response, request.getServletContext(), localeResolver.resolveLocale(request));
	}
	
	private WebContext createContext(String template, Map<String, Object> variables, HttpServletRequest request) {
		WebContext webContext = new WebContext(request, response, request.getServletContext(), localeResolver.resolveLocale(request));
		webContext.setVariables(variables);
		return webContext;
	}
	
}
