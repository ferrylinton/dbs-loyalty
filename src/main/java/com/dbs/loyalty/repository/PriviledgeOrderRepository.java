package com.dbs.loyalty.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.dbs.loyalty.domain.trx.PriviledgeOrder;

public interface PriviledgeOrderRepository extends JpaRepository<PriviledgeOrder, String>{

}
