package com.dbs.loyalty.repository;

import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import com.dbs.loyalty.domain.HealthPartner;

public interface HealthPartnerRepository extends JpaRepository<HealthPartner, String>{

	List<HealthPartner> findAll(Sort sort);
	
}
