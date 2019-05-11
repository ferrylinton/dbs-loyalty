package com.dbs.loyalty.service;

import javax.servlet.http.HttpServletRequest;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.dbs.loyalty.domain.LogApi;
import com.dbs.loyalty.repository.LogApiRepository;
import com.dbs.loyalty.service.specification.LogApiSpecification;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class LogApiService {

	private final LogApiRepository logApiRepository;
	
	public Page<LogApi> findAll(Pageable pageable, HttpServletRequest request) {
		return logApiRepository.findAll(LogApiSpecification.getSpec(request), pageable);
	}
	
	@Async
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public void save(LogApi logApi) {
		logApiRepository.save(logApi);
	}
	
}
