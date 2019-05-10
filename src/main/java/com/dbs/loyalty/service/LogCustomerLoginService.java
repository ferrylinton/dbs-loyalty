package com.dbs.loyalty.service;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.dbs.loyalty.domain.LogCustomerLogin;
import com.dbs.loyalty.repository.LogCustomerLoginRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class LogCustomerLoginService {

	private final LogCustomerLoginRepository logCustomerLoginRepository;

	@Async
	public LogCustomerLogin save(LogCustomerLogin logCustomerLogin) {
		return logCustomerLoginRepository.save(logCustomerLogin);
	}
	
}
