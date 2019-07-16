package com.dbs.loyalty.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.dbs.loyalty.domain.ass.Airport;

public interface AirportRepository extends JpaRepository<Airport, String>{
	
}
