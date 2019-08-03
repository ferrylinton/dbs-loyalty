package com.dbs.loyalty.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import com.dbs.loyalty.domain.PriviledgeProduct;

public interface PriviledgeProductRepository extends JpaRepository<PriviledgeProduct, String>, JpaSpecificationExecutor<PriviledgeProduct>{

	Optional<PriviledgeProduct> findByNameIgnoreCase(String name);
	
	@Query(value = "select p.termAndCondition from PriviledgeProduct p where id= ?1")
	Optional<String> findTermAndConditionById(String id);

}
