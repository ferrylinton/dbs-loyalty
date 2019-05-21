package com.dbs.loyalty.repository;

import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import com.dbs.loyalty.domain.MedicalProviderBranch;

public interface MedicalProviderBranchRepository extends JpaRepository<MedicalProviderBranch, String>{

	List<MedicalProviderBranch> findByMedicalProviderCityId(String medicalProviderCityId, Sort sort);
	
}
