package com.dbs.loyalty.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.dbs.loyalty.domain.TadaRecipient;

public interface TadaRecipientRepository extends JpaRepository<TadaRecipient, String>{
	
}
