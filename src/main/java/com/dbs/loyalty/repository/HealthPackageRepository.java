package com.dbs.loyalty.repository;

import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import com.dbs.loyalty.domain.med.HealthPackage;

public interface HealthPackageRepository extends JpaRepository<HealthPackage, String>{

	List<HealthPackage> findAll(Sort sort);
	
}
