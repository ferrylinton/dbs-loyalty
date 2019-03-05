package com.dbs.loyalty.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.dbs.loyalty.domain.Approval;

public interface ApprovalRepository extends JpaRepository<Approval, String>{

}
