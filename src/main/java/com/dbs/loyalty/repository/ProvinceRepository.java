package com.dbs.loyalty.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.dbs.loyalty.domain.Province;

public interface ProvinceRepository extends JpaRepository<Province, Integer>, JpaSpecificationExecutor<Province>{

	Optional<Province> findByNameIgnoreCase(String name);
	
}
