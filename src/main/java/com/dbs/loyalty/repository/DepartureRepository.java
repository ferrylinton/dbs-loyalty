package com.dbs.loyalty.repository;

import java.time.Instant;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.dbs.loyalty.domain.Departure;

public interface DepartureRepository extends JpaRepository<Departure, String>{
	
	@EntityGraph(attributePaths = { "airport", "customer" })
	Optional<Departure> findById(String id);
	
	@Query(value = "select d from Departure d "
			+ "join fetch d.customer c "
			+ "join fetch d.airport r "
			+ "where c.id = ?1 and r.id = ?2 and d.flightDate = ?3")
	Optional<Departure> findByCustomerAndAirportAndDate(String customerId, String airportId, Instant flightDate);
	
	@EntityGraph(attributePaths = { "airport", "customer" })
	Page<Departure> findAll(Pageable pageable);
	
}
