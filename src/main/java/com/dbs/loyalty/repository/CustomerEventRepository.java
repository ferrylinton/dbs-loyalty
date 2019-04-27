package com.dbs.loyalty.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.dbs.loyalty.domain.CustomerEvent;
import com.dbs.loyalty.domain.CustomerEventId;

public interface CustomerEventRepository extends JpaRepository<CustomerEvent, CustomerEventId> {

}
