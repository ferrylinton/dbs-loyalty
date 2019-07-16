package com.dbs.loyalty.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import com.dbs.loyalty.domain.Appointment;

public interface AppointmentRepository extends JpaRepository<Appointment, String>, JpaSpecificationExecutor<Appointment>{
	
	@EntityGraph(attributePaths = { "medicalProvider", "medicalProviderCity", "medicalProviderBranch", "customer" })
	Optional<Appointment> findById(String id);
	
	@Query(value = "select a from Appointment a "
			+ "join fetch a.customer c "
			+ "join fetch a.medicalProvider p "
			+ "where c.id = ?1 and p.id = ?2 and a.date = ?3")
	List<Appointment> findByCustomerAndMedicalProviderAndDate(String customerId, String medicalProviderId, LocalDate date);
	
	@EntityGraph(attributePaths = { "medicalProvider", "medicalProviderCity", "medicalProviderBranch", "customer" })
	Page<Appointment> findAll(Specification<Appointment> spec, Pageable pageable);
	
}
