package com.dbs.loyalty.security.web;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;

import com.dbs.loyalty.domain.enumeration.LoginStatus;
import com.dbs.loyalty.event.LoginEventPublisher;

public class WebAuthenticationFailureHandler extends SimpleUrlAuthenticationFailureHandler {
	
	private final LoginEventPublisher loginEventPublisher;
	
	public WebAuthenticationFailureHandler(LoginEventPublisher loginEventPublisher) {
        super.setDefaultFailureUrl("/login?error");
        this.loginEventPublisher = loginEventPublisher;
	}
	
	@Override
	public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
		loginEventPublisher.publish(LoginStatus.FAILED, request);
		super.onAuthenticationFailure(request, response, exception);
	}
}
