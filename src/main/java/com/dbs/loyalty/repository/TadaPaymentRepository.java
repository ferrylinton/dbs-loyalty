package com.dbs.loyalty.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.dbs.loyalty.domain.TadaPayment;

public interface TadaPaymentRepository extends JpaRepository<TadaPayment, String>{
	
}
