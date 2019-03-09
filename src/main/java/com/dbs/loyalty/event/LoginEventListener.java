package com.dbs.loyalty.event;

import org.springframework.context.ApplicationListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import com.dbs.loyalty.service.LogLoginService;

@Component
public class LoginEventListener implements ApplicationListener<LoginEvent> {
		
	private final LogLoginService logLoginService;
	
	public LoginEventListener(LogLoginService logLoginService) {
		this.logLoginService = logLoginService;
	}

	@Async
	@Override
	public void onApplicationEvent(LoginEvent event) {
		logLoginService.save(event.getLoginLog());
	}
	
}
