package com.dbs.loyalty.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import com.dbs.loyalty.domain.City;
import com.dbs.loyalty.projection.NameOnly;

public interface CityRepository extends JpaRepository<City, Integer>{

	Optional<City> findByNameIgnoreCase(String name);
	
	List<NameOnly> findFirst10ByNameContainingIgnoreCase(String name, Sort sort);
	
}
