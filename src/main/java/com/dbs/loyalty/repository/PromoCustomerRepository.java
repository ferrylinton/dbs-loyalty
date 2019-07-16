package com.dbs.loyalty.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.dbs.loyalty.domain.prm.PromoCustomer;
import com.dbs.loyalty.domain.prm.PromoCustomerId;

public interface PromoCustomerRepository extends JpaRepository<PromoCustomer, PromoCustomerId>{

	@Query(value = "select p from PromoCustomer p "
			+ "join fetch p.customer c "
			+ "where p.id.promoId = ?1", 
			countQuery = "select count(p) from PromoCustomer p "
					+ "where p.id.promoId = ?1")
	Page<PromoCustomer> findByPromoId(String promoId, Pageable pageable);
	
	@Query(value = "select p from PromoCustomer p "
			+ "join fetch p.customer c "
			+ "where p.id.promoId = ?1 and (lower(c.name) like ?2 or lower(c.email) like ?2 or lower(c.phone) like ?2)", 
			countQuery = "select count(p) from PromoCustomer p "
					+ "join p.customer c "
					+ "where p.id.promoId = ?1 and (lower(c.name) like ?2 or lower(c.email) like ?2 or lower(c.phone) like ?2)")
	Page<PromoCustomer> findByPromoIdAndKeyword(String promoId, String keyword, Pageable pageable);
	
}
