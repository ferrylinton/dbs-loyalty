package com.dbs.loyalty.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.dbs.loyalty.domain.Address;

public interface AddressRepository extends JpaRepository<Address, String>{
	
	Optional<Address> findByCustomerIdAndLabelIgnoreCase(String customerId, String label);
	
	Optional<Address> findByCustomerEmailAndLabelIgnoreCase(String email, String label);
	
	@Query(value = "from Address a "
			+ "JOIN FETCH a.customer c "
			+ "JOIN FETCH a.city ct "
			+ "where c.email = ?1")
	List<Address> findByCustomerEmail(String customerEmail);
	
}
