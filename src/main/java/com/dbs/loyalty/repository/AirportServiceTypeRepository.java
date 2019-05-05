package com.dbs.loyalty.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.dbs.loyalty.domain.AirportServiceType;

public interface AirportServiceTypeRepository extends JpaRepository<AirportServiceType, String>{
	
}
