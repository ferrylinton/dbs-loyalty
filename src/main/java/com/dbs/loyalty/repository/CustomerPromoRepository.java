package com.dbs.loyalty.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.dbs.loyalty.domain.CustomerPromo;

public interface CustomerPromoRepository extends JpaRepository<CustomerPromo, String>{

}
