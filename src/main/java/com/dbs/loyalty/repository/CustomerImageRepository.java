package com.dbs.loyalty.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.dbs.loyalty.domain.CustomerImage;

public interface CustomerImageRepository extends JpaRepository<CustomerImage, String>{

	Optional<CustomerImage> findByCustomerEmail(String email);
	
}
