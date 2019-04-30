package com.dbs.loyalty.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.dbs.loyalty.domain.EventCustomerId;
import com.dbs.loyalty.domain.EventCustomer;

public interface EventCustomerRepository extends JpaRepository<EventCustomer, EventCustomerId> {

	@Query(value = "select e from EventCustomer e "
			+ "join fetch e.customer c "
			+ "where e.id.eventId = ?1", 
			countQuery = "select count(e) from EventCustomer e "
					+ "where e.id.eventId = ?1")
	Page<EventCustomer> findWithCustomerByEventId(String eventId, Pageable pageable);
	
}
