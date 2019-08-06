package com.dbs.loyalty.repository;

import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.dbs.loyalty.domain.MedicalCity;

public interface MedicalCityRepository extends JpaRepository<MedicalCity, String>, JpaSpecificationExecutor<MedicalCity>{

	List<MedicalCity> findByMedicalProviderId(String medicalProviderId, Sort sort);
	
}
