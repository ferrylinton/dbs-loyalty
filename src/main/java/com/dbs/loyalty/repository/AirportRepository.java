package com.dbs.loyalty.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.lang.Nullable;

import com.dbs.loyalty.domain.Airport;

public interface AirportRepository extends JpaRepository<Airport, String>, JpaSpecificationExecutor<Airport>{
	
	@EntityGraph(attributePaths = { "country" })
	Optional<Airport> findById(String id);
	
	@EntityGraph(attributePaths = { "country" })
	Page<Airport> findAll(@Nullable Specification<Airport> spec, Pageable pageable);
	
}
