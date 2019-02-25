package com.dbs.priviledge.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.dbs.priviledge.domain.Customer;

public interface CustomerRepository extends JpaRepository<Customer, String>{

	Page<Customer> findAll(Pageable pageable);
	
	Page<Customer> findAllByEmailContainingAllIgnoreCase(String email, Pageable pageable);

	Optional<Customer> findByEmail(String email);
	
	Optional<Customer> findByEmailIgnoreCase(String email);
	
}
