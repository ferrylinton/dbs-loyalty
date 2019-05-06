package com.dbs.loyalty.repository;

import java.time.LocalDate;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.dbs.loyalty.domain.Appointment;

public interface AppointmentRepository extends JpaRepository<Appointment, String>{
	
	@EntityGraph(attributePaths = { "healthPartner", "customer" })
	Optional<Appointment> findById(String id);
	
	@Query(value = "select a from Appointment a "
			+ "join fetch a.customer c "
			+ "join fetch a.healthPartner r "
			+ "where c.id = ?1 and r.id = ?2 and a.arrivalDate = ?3")
	Optional<Appointment> findByCustomerAndHealthPartnerAndDate(String customerId, String airportId, LocalDate arrivalDate);
	
	@EntityGraph(attributePaths = { "healthPartner", "customer" })
	Page<Appointment> findAll(Pageable pageable);
	
}
