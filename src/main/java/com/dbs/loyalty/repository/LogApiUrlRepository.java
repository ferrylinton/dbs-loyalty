package com.dbs.loyalty.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.dbs.loyalty.domain.LogApiUrl;

public interface LogApiUrlRepository extends JpaRepository<LogApiUrl, String>{
	
}
