package com.dbs.loyalty.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.lang.Nullable;

import com.dbs.loyalty.domain.Reward;

public interface RewardRepository extends JpaRepository<Reward, String>, JpaSpecificationExecutor<Reward>{

	@EntityGraph(attributePaths = {"customer"})
	Optional<Reward> findByCustomerId(String id);
	
	@EntityGraph(attributePaths = { "customer" })
	Page<Reward> findAll(@Nullable Specification<Reward> spec, Pageable pageable);
	
	@Query(value = "select r from Reward r "
			+ "join fetch r.customer c "
			+ "where c.id = ?1 and r.rewardType = 'debit' and r.point > 0 and r.expiryDate > CURRENT_DATE")
	List<Reward> findAllValid(String customerId);
	
	
}
