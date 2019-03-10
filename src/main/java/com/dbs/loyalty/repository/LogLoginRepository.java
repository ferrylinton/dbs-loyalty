package com.dbs.loyalty.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.dbs.loyalty.domain.LogLogin;

public interface LogLoginRepository extends JpaRepository<LogLogin, String>, JpaSpecificationExecutor<LogLogin>{

}
