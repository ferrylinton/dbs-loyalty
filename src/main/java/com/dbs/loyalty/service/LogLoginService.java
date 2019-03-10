package com.dbs.loyalty.service;

import javax.servlet.http.HttpServletRequest;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.dbs.loyalty.domain.LogLogin;
import com.dbs.loyalty.repository.LogLoginRepository;
import com.dbs.loyalty.service.specification.LogLoginSpecification;

@Service
public class LogLoginService {

	private final LogLoginRepository logLoginRepository;

	public LogLoginService(LogLoginRepository logLoginRepository) {
		this.logLoginRepository = logLoginRepository;
	}

	public LogLogin save(LogLogin logLogin) {
		return logLoginRepository.save(logLogin);
	}
	
	public Page<LogLogin> findAll(Pageable pageable, HttpServletRequest request) {
		Specification<LogLogin> spec = LogLoginSpecification.getSpec(request);
		return logLoginRepository.findAll(spec, pageable);
	}
	
}
