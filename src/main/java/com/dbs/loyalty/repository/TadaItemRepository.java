package com.dbs.loyalty.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.dbs.loyalty.domain.TadaItem;

public interface TadaItemRepository extends JpaRepository<TadaItem, String>{
	
}
