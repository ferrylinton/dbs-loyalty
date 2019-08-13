package com.dbs.loyalty.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.lang.Nullable;

import com.dbs.loyalty.domain.Province;

public interface ProvinceRepository extends JpaRepository<Province, Integer>, JpaSpecificationExecutor<Province>{

	@EntityGraph(attributePaths = {"country"})
	Optional<Province> findWithCountryById(Integer id);
	
	Optional<Province> findByNameIgnoreCase(String name);
	
	@EntityGraph(attributePaths = { "country" })
	Page<Province> findAll(@Nullable Specification<Province> spec, Pageable pageable);
	
}
