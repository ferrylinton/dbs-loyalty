package com.dbs.loyalty.event;

import javax.servlet.http.HttpServletRequest;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import com.dbs.loyalty.domain.LogLogin;
import com.dbs.loyalty.domain.enumeration.LoginStatus;
import com.dbs.loyalty.service.BrowserService;

@Component
public class LoginEventPublisher{

	private final ApplicationEventPublisher publisher;
	
	private final BrowserService browserService;
	
	public LoginEventPublisher(ApplicationEventPublisher publisher, BrowserService browserService) {
		this.publisher = publisher;
		this.browserService = browserService;
	}

	public void publish(LoginStatus loginStatus, HttpServletRequest request) {
		LogLogin logLogin = browserService.createLogLogin(loginStatus, request);
		publisher.publishEvent(new LoginEvent(this, logLogin));
	}

}