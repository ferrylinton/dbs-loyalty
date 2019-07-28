package com.dbs.loyalty.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.dbs.loyalty.domain.LogAuditCustomer;

public interface LogAuditCustomerRepository extends JpaRepository<LogAuditCustomer, String>, JpaSpecificationExecutor<LogAuditCustomer>{
	
}
