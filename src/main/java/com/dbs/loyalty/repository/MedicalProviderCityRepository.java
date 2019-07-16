package com.dbs.loyalty.repository;

import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import com.dbs.loyalty.domain.med.MedicalProviderCity;

public interface MedicalProviderCityRepository extends JpaRepository<MedicalProviderCity, String>{

	List<MedicalProviderCity> findByMedicalProviderId(String medicalProviderId, Sort sort);
	
}
