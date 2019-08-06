package com.dbs.loyalty.repository;

import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.dbs.loyalty.domain.MedicalProvider;

public interface MedicalProviderRepository extends JpaRepository<MedicalProvider, String>, JpaSpecificationExecutor<MedicalProvider>{

	List<MedicalProvider> findAll(Sort sort);
	
}
