package com.dbs.loyalty.repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.dbs.loyalty.domain.Arrival;

public interface ArrivalRepository extends JpaRepository<Arrival, String>{
	
	@EntityGraph(attributePaths = { "airport", "customer" })
	Optional<Arrival> findById(String id);
	
	@Query(value = "select a from Arrival a "
			+ "join fetch a.customer c "
			+ "join fetch a.airport r "
			+ "where c.id = ?1 and r.id = ?2 and a.flightDate = ?3 and a.flightTime = ?4")
	Optional<Arrival> findByCustomerAndAirportAndDate(String customerId, String airportId, LocalDate flightDate, LocalTime flightTime);
	
	@EntityGraph(attributePaths = { "airport", "customer" })
	Page<Arrival> findAll(Pageable pageable);
	
}
