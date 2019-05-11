package com.dbs.loyalty.security.web;

import static com.dbs.loyalty.config.constant.StatusConstant.SUCCEED;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.scheduling.annotation.Async;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;

import com.dbs.loyalty.service.BrowserService;
import com.dbs.loyalty.service.LogLoginService;

public class WebAuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
	
	private final LogLoginService logLoginService;
	
	private final BrowserService browserService;
	
	public WebAuthenticationSuccessHandler(LogLoginService logLoginService, BrowserService browserService) {
		super.setDefaultTargetUrl("/home");
		this.logLoginService = logLoginService;
        this.browserService = browserService;
	}
	
	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
		save(request);
		super.onAuthenticationSuccess(request, response, authentication);
	}
	
	@Async
	private void save(HttpServletRequest request) {
		logLoginService.save(browserService.createLogLogin(SUCCEED, request));
	}
	
}
