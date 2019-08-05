package com.dbs.loyalty.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.lang.Nullable;

import com.dbs.loyalty.domain.TadaOrder;

public interface TadaOrderRepository extends JpaRepository<TadaOrder, String>, JpaSpecificationExecutor<TadaOrder>{
	
	@EntityGraph(attributePaths = { "tadaPayment", "tadaRecipient", "tadaItems" })
	Optional<TadaOrder> findById(String id);
	
	@EntityGraph(attributePaths = { "tadaPayment", "tadaRecipient", "tadaItems" })
	Page<TadaOrder> findAll(@Nullable Specification<TadaOrder> spec, Pageable pageable);

	Optional<TadaOrder> findByOrderReference(String orderReference);
	
	Optional<TadaOrder> findByOrderReferenceAndCreatedBy(String orderReference, String createdBy);

}
