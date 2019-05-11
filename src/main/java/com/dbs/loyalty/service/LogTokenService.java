package com.dbs.loyalty.service;

import javax.servlet.http.HttpServletRequest;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.dbs.loyalty.domain.LogToken;
import com.dbs.loyalty.repository.LogTokenRepository;
import com.dbs.loyalty.service.specification.LogTokenSpecification;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class LogTokenService {

	private final LogTokenRepository logTokenRepository;

	public Page<LogToken> findAll(Pageable pageable, HttpServletRequest request) {
		return logTokenRepository.findAll(LogTokenSpecification.getSpec(request), pageable);
	}
	
	@Async
	public LogToken save(LogToken logToken) {
		return logTokenRepository.save(logToken);
	}
	
}
