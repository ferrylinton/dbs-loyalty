package com.dbs.priviledge.repository;

import java.time.LocalDate;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import com.dbs.priviledge.domain.Reward;

public interface RewardRepository extends JpaRepository<Reward, String>{

	@EntityGraph(attributePaths = { "rewardOperation" })
	Page<Reward> findByExpiryDateGreaterThanEqualAndCustomerEmailAndPointGreaterThanOrderByExpiryDateDesc(LocalDate expiryDate, String email, Integer point, Pageable pageable);
	
}
