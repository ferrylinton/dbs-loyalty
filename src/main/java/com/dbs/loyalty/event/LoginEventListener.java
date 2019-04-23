package com.dbs.loyalty.event;

import org.springframework.context.ApplicationListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import com.dbs.loyalty.service.LogLoginService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Component
public class LoginEventListener implements ApplicationListener<LoginEvent> {
		
	private final LogLoginService logLoginService;

	@Async
	@Override
	public void onApplicationEvent(LoginEvent event) {
		logLoginService.save(event.getLoginLog());
	}
	
}
