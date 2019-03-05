package com.dbs.loyalty.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.dbs.loyalty.domain.LovedOne;

public interface LovedOneRepository extends JpaRepository<LovedOne, String>{

}
