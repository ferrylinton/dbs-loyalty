package com.dbs.loyalty.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.dbs.loyalty.domain.log.LogToken;

public interface LogTokenRepository extends JpaRepository<LogToken, String>, JpaSpecificationExecutor<LogToken>{

	
}
