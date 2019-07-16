package com.dbs.loyalty.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.dbs.loyalty.domain.ass.AirportAssistance;

public interface AirportAssistanceRepository extends JpaRepository<AirportAssistance, String>{
	
}
