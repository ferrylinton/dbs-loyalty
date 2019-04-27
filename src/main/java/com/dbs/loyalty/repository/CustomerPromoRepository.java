package com.dbs.loyalty.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.dbs.loyalty.domain.PromoCustomer;
import com.dbs.loyalty.domain.PromoCustomerId;

public interface CustomerPromoRepository extends JpaRepository<PromoCustomer, PromoCustomerId>{

}
