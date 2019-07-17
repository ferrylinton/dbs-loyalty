package com.dbs.loyalty.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.dbs.loyalty.domain.Province;

public interface ProvinceRepository extends JpaRepository<Province, Integer>{

	Optional<Province> findByNameIgnoreCase(String name);
	
}
