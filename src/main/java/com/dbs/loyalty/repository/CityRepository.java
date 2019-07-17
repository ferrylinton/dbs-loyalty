package com.dbs.loyalty.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.dbs.loyalty.domain.City;

public interface CityRepository extends JpaRepository<City, Integer>{

	Optional<City> findByNameIgnoreCase(String name);
	
}
