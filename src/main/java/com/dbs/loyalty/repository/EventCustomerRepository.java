package com.dbs.loyalty.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.dbs.loyalty.domain.evt.EventCustomer;
import com.dbs.loyalty.domain.evt.EventCustomerId;

public interface EventCustomerRepository extends JpaRepository<EventCustomer, EventCustomerId> {

	@Query(value = "select e from EventCustomer e "
			+ "join fetch e.customer c "
			+ "where e.id.eventId = ?1", 
			countQuery = "select count(e) from EventCustomer e "
					+ "where e.id.eventId = ?1")
	Page<EventCustomer> findByEventId(String eventId, Pageable pageable);
	
	@Query(value = "select e from EventCustomer e "
			+ "join fetch e.customer c "
			+ "where e.id.eventId = ?1 and (lower(c.name) like ?2 or lower(c.email) like ?2 or lower(c.phone) like ?2)", 
			countQuery = "select count(e) from EventCustomer e "
					+ "join e.customer c "
					+ "where e.id.eventId = ?1 and (lower(c.name) like ?2 or lower(c.email) like ?2 or lower(c.phone) like ?2)")
	Page<EventCustomer> findByEventIdAndKeyword(String eventId, String keyword, Pageable pageable);
	
}
