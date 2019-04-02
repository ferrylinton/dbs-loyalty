package com.dbs.loyalty.security.web;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;

import com.dbs.loyalty.domain.enumeration.LoginStatus;
import com.dbs.loyalty.event.LoginEventPublisher;

public class WebAuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
	
	private final LoginEventPublisher loginEventPublisher;
	
	public WebAuthenticationSuccessHandler(LoginEventPublisher loginEventPublisher) {
		super.setDefaultTargetUrl("/home");
		this.loginEventPublisher = loginEventPublisher;
	}
	
	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
		loginEventPublisher.publish(LoginStatus.SUCCEEDED, request);
		super.onAuthenticationSuccess(request, response, authentication);
	}
	
}
