package com.dbs.loyalty.security.web;

import static com.dbs.loyalty.config.constant.StatusConstant.FAIL;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.scheduling.annotation.Async;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;

import com.dbs.loyalty.service.BrowserService;
import com.dbs.loyalty.service.LogLoginService;

public class WebAuthenticationFailureHandler extends SimpleUrlAuthenticationFailureHandler {
	
	private final LogLoginService logLoginService;
	
	private final BrowserService browserService;
	
	public WebAuthenticationFailureHandler(LogLoginService logLoginService, BrowserService browserService) {
        super.setDefaultFailureUrl("/login?error");
        this.logLoginService = logLoginService;
        this.browserService = browserService;
	}
	
	@Override
	public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
		save(request);
		super.onAuthenticationFailure(request, response, exception);
	}
	
	@Async
	private void save(HttpServletRequest request) {
		logLoginService.save(browserService.createLogLogin(FAIL, request));
	}
}
