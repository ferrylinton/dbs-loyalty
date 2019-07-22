package com.dbs.loyalty.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.dbs.loyalty.domain.TadaOrder;

public interface TadaOrderRepository extends JpaRepository<TadaOrder, String>, JpaSpecificationExecutor<TadaOrder>{
	
	Optional<TadaOrder> findByOrderReference(String orderReference);
	
	Optional<TadaOrder> findByOrderReferenceAndCreatedBy(String orderReference, String createdBy);

}
