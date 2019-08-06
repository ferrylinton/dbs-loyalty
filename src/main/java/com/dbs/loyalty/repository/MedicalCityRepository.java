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

import com.dbs.loyalty.domain.MedicalCity;

public interface MedicalCityRepository extends JpaRepository<MedicalCity, String>, JpaSpecificationExecutor<MedicalCity>{

	@EntityGraph(attributePaths = { "medicalProvider" })
	Optional<MedicalCity> findById(String id);
	
	List<MedicalCity> findByMedicalProviderId(String medicalProviderId, Sort sort);
	
	@EntityGraph(attributePaths = { "medicalProvider" })
	Page<MedicalCity> findAll(@Nullable Specification<MedicalCity> spec, Pageable pageable);
	
}
