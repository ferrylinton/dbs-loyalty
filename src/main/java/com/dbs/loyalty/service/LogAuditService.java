package com.dbs.loyalty.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.dbs.loyalty.domain.LogAudit;
import com.dbs.loyalty.repository.LogAuditRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class LogAuditService {

	private final LogAuditRepository logAuditRepository;
	
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public void save(LogAudit logAudit) {
		logAuditRepository.save(logAudit);
	}
	
}
