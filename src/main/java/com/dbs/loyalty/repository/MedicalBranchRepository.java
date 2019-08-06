package com.dbs.loyalty.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.lang.Nullable;

import com.dbs.loyalty.domain.MedicalBranch;

public interface MedicalBranchRepository extends JpaRepository<MedicalBranch, String>, JpaSpecificationExecutor<MedicalBranch>{

	@EntityGraph(attributePaths = { "medicalCity", "medicalCity.medicalProvider" })
	Optional<MedicalBranch> findById(String id);
	
	List<MedicalBranch> findByMedicalCityId(String medicalCityId, Sort sort);
	
	@EntityGraph(attributePaths = { "medicalCity", "medicalCity.medicalProvider" })
	Page<MedicalBranch> findAll(@Nullable Specification<MedicalBranch> spec, Pageable pageable);
	
}
