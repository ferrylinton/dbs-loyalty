package com.dbs.loyalty.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.dbs.loyalty.domain.TrxProduct;

public interface TrxProductRepository extends JpaRepository<TrxProduct, String>, JpaSpecificationExecutor<TrxProduct>{
	

}
