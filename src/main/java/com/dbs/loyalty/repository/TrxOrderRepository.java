package com.dbs.loyalty.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.dbs.loyalty.domain.TrxOrder;

public interface TrxOrderRepository extends JpaRepository<TrxOrder, String>, JpaSpecificationExecutor<TrxOrder>{
	

}
