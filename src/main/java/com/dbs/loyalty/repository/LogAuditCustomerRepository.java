package com.dbs.loyalty.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.lang.Nullable;

import com.dbs.loyalty.domain.LogAuditCustomer;

public interface LogAuditCustomerRepository extends JpaRepository<LogAuditCustomer, String>, JpaSpecificationExecutor<LogAuditCustomer>{
	
	@EntityGraph(attributePaths = { "customer" })
	Optional<LogAuditCustomer> findWithCustomerById(String id);
	
	@EntityGraph(attributePaths = { "customer" })
	Page<LogAuditCustomer> findAll(@Nullable Specification<LogAuditCustomer> spec, Pageable pageable);
	
}
