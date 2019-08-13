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

import com.dbs.loyalty.domain.City;
import com.dbs.loyalty.projection.NameOnly;

public interface CityRepository extends JpaRepository<City, Integer>, JpaSpecificationExecutor<City>{

	@EntityGraph(attributePaths = { "province", "province.country"})
	Optional<City> findWithProvinceById(Integer id);
	
	Optional<City> findByNameIgnoreCase(String name);
	
	List<NameOnly> findFirst10ByNameContainingIgnoreCase(String name, Sort sort);
	
	@EntityGraph(attributePaths = { "province", "province.country" })
	Page<City> findAll(@Nullable Specification<City> spec, Pageable pageable);
	
}
