package com.dbs.loyalty.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.dbs.loyalty.domain.TadaOrder;

public interface TadaOrderRepository extends JpaRepository<TadaOrder, String>{
	
}
