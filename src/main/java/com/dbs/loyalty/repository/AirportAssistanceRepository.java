package com.dbs.loyalty.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.dbs.loyalty.domain.AirportAssistance;
import com.dbs.loyalty.domain.enumeration.FlightType;

public interface AirportAssistanceRepository extends JpaRepository<AirportAssistance, String>{
	
	@Query(value = "select a from AirportAssistance a "
			+ "join fetch a.customer "
			+ "join fetch a.airport "
			+ "where a.id = ?1 ")
	Optional<AirportAssistance> findById(String id);
	
	@Query(value = "select a from AirportAssistance a "
			+ "join fetch a.customer "
			+ "join fetch a.airport "
			+ "where a.flightType = ?1", 
			countQuery = "select count(a) from AirportAssistance a where a.flightType = ?1")
	Page<AirportAssistance> findByFlightType(FlightType flightType, Pageable pageable);
	
}
