package com.dbs.loyalty.repository;

import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import com.dbs.loyalty.domain.Country;

public interface CountryRepository extends JpaRepository<Country, Integer>{
	
	@EntityGraph(attributePaths = { "airports" })
	List<Country> findAll(Sort sort);
	
}
