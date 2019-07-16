package com.dbs.loyalty.service;

import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.dbs.loyalty.domain.log.LogToken;
import com.dbs.loyalty.repository.LogTokenRepository;
import com.dbs.loyalty.service.specification.LogTokenSpec;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class LogTokenService {

	private final LogTokenRepository logTokenRepository;

	public Page<LogToken> findAll(Map<String, String> params, Pageable pageable) {
		return logTokenRepository.findAll(new LogTokenSpec(params), pageable);
	}
	
	@Async
	public LogToken save(LogToken logToken) {
		return logTokenRepository.save(logToken);
	}
	
}
