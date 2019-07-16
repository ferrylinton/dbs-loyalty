package com.dbs.loyalty.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.dbs.loyalty.domain.LogApi;

public interface LogApiRepository extends JpaRepository<LogApi, String>, JpaSpecificationExecutor<LogApi>{
	
}
