package com.dbs.loyalty.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.dbs.loyalty.domain.PriviledgeOrder;

public interface PriviledgeOrderRepository extends JpaRepository<PriviledgeOrder, String>, JpaSpecificationExecutor<PriviledgeOrder>{

}
