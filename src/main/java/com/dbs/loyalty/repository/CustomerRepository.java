package com.dbs.loyalty.repository;

import java.util.Optional;

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

	@EntityGraph(attributePaths = { "country" })
	Optional<Customer> findById(String id);
	
	Optional<Customer> findByEmail(String email);
	
	Optional<Customer> findByEmailIgnoreCase(String email);
	
	Optional<Customer> findByNameIgnoreCaseOrEmailIgnoreCase(String name, String email);
	
	@Transactional(propagation=Propagation.REQUIRED)
	@Modifying
	@Query("update Customer c set c.passwordHash = :passwordHash where c.email = :email")
	void changePassword(@Param("passwordHash") String passwordHash, @Param("email") String email);

	@Modifying
	@Query("update Customer c set c.pending = ?1 where c.id = ?2")
	void save(boolean pending, String id);
	
}
