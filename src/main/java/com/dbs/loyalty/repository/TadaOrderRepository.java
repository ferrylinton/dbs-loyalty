package com.dbs.loyalty.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.dbs.loyalty.domain.trx.TadaOrder;

public interface TadaOrderRepository extends JpaRepository<TadaOrder, String>{
	
}
