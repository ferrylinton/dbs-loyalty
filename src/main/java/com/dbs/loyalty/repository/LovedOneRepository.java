package com.dbs.loyalty.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.dbs.loyalty.domain.LovedOne;

public interface LovedOneRepository extends JpaRepository<LovedOne, String>, JpaSpecificationExecutor<LovedOne>{

	@Query(value = "from LovedOne l "
			+ "JOIN FETCH l.customer c "
			+ "where c.email=:customerEmail")
	List<LovedOne> findByCustomerEmail(@Param("customerEmail") String customerEmail);
	
	Optional<LovedOne> findByNameIgnoreCaseAndCustomerEmail(String name, String email);
	
}
