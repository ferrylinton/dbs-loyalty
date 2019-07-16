package com.dbs.loyalty.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.dbs.loyalty.domain.Address;

public interface AddressRepository extends JpaRepository<Address, String>{
	
}
