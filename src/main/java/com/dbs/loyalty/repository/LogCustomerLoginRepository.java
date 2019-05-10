package com.dbs.loyalty.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.dbs.loyalty.domain.LogCustomerLogin;

public interface LogCustomerLoginRepository extends JpaRepository<LogCustomerLogin, String>{

	
}
