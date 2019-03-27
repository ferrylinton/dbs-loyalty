package com.dbs.loyalty.event;

import org.springframework.context.ApplicationListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import com.dbs.loyalty.service.LogLoginService;
import com.dbs.loyalty.service.dto.LogLoginDto;
import com.dbs.loyalty.service.mapper.LogLoginMapper;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Component
public class LoginEventListener implements ApplicationListener<LoginEvent> {
		
	private final LogLoginService logLoginService;
	
	private final LogLoginMapper logLoginMapper;
	
	@Async
	@Override
	public void onApplicationEvent(LoginEvent event) {
		LogLoginDto logLoginDto = logLoginMapper.toDto(event.getLoginLog());
		logLoginService.save(logLoginDto);
	}
	
}
