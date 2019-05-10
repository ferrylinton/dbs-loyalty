package com.dbs.loyalty.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.dbs.loyalty.domain.LogAudit;

public interface LogAuditRepository extends JpaRepository<LogAudit, String>{
	
}
