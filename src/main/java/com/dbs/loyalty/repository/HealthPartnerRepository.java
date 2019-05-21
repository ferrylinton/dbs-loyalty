package com.dbs.loyalty.repository;

import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import com.dbs.loyalty.domain.MedicalProvider;

public interface HealthPartnerRepository extends JpaRepository<MedicalProvider, String>{

	List<MedicalProvider> findAll(Sort sort);
	
}
