package com.dbs.loyalty.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.dbs.loyalty.domain.City;
import com.dbs.loyalty.projection.NameOnly;

public interface CityRepository extends JpaRepository<City, Integer>, JpaSpecificationExecutor<City>{

	Optional<City> findByNameIgnoreCase(String name);
	
	List<NameOnly> findFirst10ByNameContainingIgnoreCase(String name, Sort sort);
	
}
