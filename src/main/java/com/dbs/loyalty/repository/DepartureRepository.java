package com.dbs.loyalty.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.dbs.loyalty.domain.Departure;

public interface DepartureRepository extends JpaRepository<Departure, String>{
	
	@Query(value = "select d from Departure d "
			+ "join fetch d.customer c "
			+ "join fetch d.airport r ")
	Optional<Departure> findById(String id);
	
	@Query(value = "select d from Departure d "
			+ "join fetch d.customer c "
			+ "join fetch d.airport r ", 
			countQuery = "select count(d) from Departure d")
	Page<Departure> findAll(Pageable pageable);
	
}
