package com.dbs.loyalty.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.dbs.loyalty.domain.Arrival;

public interface ArrivalRepository extends JpaRepository<Arrival, String>{
	
	@Query(value = "select a from Arrival a "
			+ "join fetch a.customer c "
			+ "join fetch a.airport r ")
	Optional<Arrival> findById(String id);
	
	@Query(value = "select a from Arrival a "
			+ "join fetch a.customer c "
			+ "join fetch a.airport r ", 
			countQuery = "select count(a) from Arrival a")
	Page<Arrival> findAll(Pageable pageable);
	
}
