package com.dbs.loyalty.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.dbs.loyalty.domain.Airport;

public interface AirportRepository extends JpaRepository<Airport, String>{
	
}
