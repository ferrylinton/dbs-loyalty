package com.dbs.loyalty.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.dbs.loyalty.domain.Address;

public interface AddressRepository extends JpaRepository<Address, String>{
	
	@Query(value = "from Address a "
			+ "JOIN FETCH a.customer c "
			+ "JOIN FETCH a.city ct "
			+ "where c.email = ?1")
	List<Address> findByCustomerEmail(String customerEmail);
	
}
