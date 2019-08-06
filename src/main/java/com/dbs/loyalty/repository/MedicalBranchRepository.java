package com.dbs.loyalty.repository;

import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.dbs.loyalty.domain.MedicalBranch;

public interface MedicalBranchRepository extends JpaRepository<MedicalBranch, String>, JpaSpecificationExecutor<MedicalBranch>{

	List<MedicalBranch> findByMedicalCityId(String medicalCityId, Sort sort);
	
}
