package com.dbs.loyalty.repository;

import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.dbs.loyalty.domain.HealthPackage;

public interface HealthPackageRepository extends JpaRepository<HealthPackage, String>, JpaSpecificationExecutor<HealthPackage>{

	List<HealthPackage> findAll(Sort sort);
	
}
