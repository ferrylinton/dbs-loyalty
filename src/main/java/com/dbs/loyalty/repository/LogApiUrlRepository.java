package com.dbs.loyalty.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.dbs.loyalty.domain.LogApiUrl;

public interface LogApiUrlRepository extends JpaRepository<LogApiUrl, String>, JpaSpecificationExecutor<LogApiUrl>{
	
	Optional<LogApiUrl> findByUrlIgnoreCase(String url);
	
}
