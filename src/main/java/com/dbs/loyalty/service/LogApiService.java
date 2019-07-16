package com.dbs.loyalty.service;

import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.dbs.loyalty.domain.log.LogApi;
import com.dbs.loyalty.repository.LogApiRepository;
import com.dbs.loyalty.service.specification.LogApiSpec;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class LogApiService {

	private final LogApiRepository logApiRepository;
	
	public Page<LogApi> findAll(Map<String, String> params, Pageable pageable) {
		return logApiRepository.findAll(new LogApiSpec(params), pageable);
	}
	
	@Async
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public void save(LogApi logApi) {
		logApiRepository.save(logApi);
	}
	
}
