package com.dbs.loyalty.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.dbs.loyalty.domain.Customer;

public interface CustomerRepository extends JpaRepository<Customer, String>, JpaSpecificationExecutor<Customer>{

	@EntityGraph(attributePaths = { "customerImage" })
	Optional<Customer> findWithCustomerImageById(String id);
	
	Optional<Customer> findByEmail(String email);
	
	Optional<Customer> findByEmailIgnoreCase(String email);
	
	@EntityGraph(attributePaths = { "customerImage" })
	Page<Customer> findAll(Specification<Customer> spec, Pageable pageable);
	
	@Transactional(propagation=Propagation.REQUIRED)
	@Modifying
	@Query("update Customer c set c.passwordHash = :passwordHash where c.email = :email")
	void changePassword(@Param("passwordHash") String passwordHash, @Param("email") String email);

}
