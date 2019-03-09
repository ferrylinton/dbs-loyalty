package com.dbs.loyalty.service;

import org.springframework.stereotype.Service;

import com.dbs.loyalty.domain.LogLogin;
import com.dbs.loyalty.repository.LogLoginRepository;

@Service
public class LogLoginService {

	private final LogLoginRepository logLoginRepository;

	public LogLoginService(LogLoginRepository logLoginRepository) {
		this.logLoginRepository = logLoginRepository;
	}

	public LogLogin save(LogLogin logLogin) {
		return logLoginRepository.save(logLogin);
	}
	
}
