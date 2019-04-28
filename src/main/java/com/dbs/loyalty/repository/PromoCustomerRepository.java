package com.dbs.loyalty.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.dbs.loyalty.domain.PromoCustomer;
import com.dbs.loyalty.domain.PromoCustomerId;

public interface PromoCustomerRepository extends JpaRepository<PromoCustomer, PromoCustomerId>{

	@Query(value = "select p from PromoCustomer p "
			+ "join fetch p.customer c "
			+ "where p.id.promoId = ?1", 
			countQuery = "select count(p) from PromoCustomer p "
					+ "where p.id.promoId = ?1")
	Page<PromoCustomer> findWithCustomerByPromoId(String promoId, Pageable pageable);
	
}
