package com.dbs.loyalty.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.dbs.loyalty.domain.TravelAssistance;

public interface TravelAssistanceRepository extends JpaRepository<TravelAssistance, String>{
	
}
