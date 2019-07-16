package com.dbs.loyalty.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.dbs.loyalty.domain.cst.Reward;

public interface RewardRepository extends JpaRepository<Reward, String>{

	@Query(value = "select r from Reward r "
			+ "join fetch r.customer c "
			+ "where c.id = ?1 and r.point > 0 and r.expiryDate >= CURRENT_DATE")
	List<Reward> findAllValid(String customerId);
	
}
