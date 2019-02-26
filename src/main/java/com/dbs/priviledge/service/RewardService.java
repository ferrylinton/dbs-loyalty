package com.dbs.priviledge.service;

import java.time.LocalDate;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.dbs.priviledge.domain.Reward;
import com.dbs.priviledge.repository.RewardRepository;

@Service
public class RewardService {

	private final RewardRepository rewardRepository;
	
	public RewardService(RewardRepository rewardRepository) {
		this.rewardRepository = rewardRepository;
	}

	public Page<Reward> findByExpiryDate(String email, Pageable pageable){
		return rewardRepository.findByExpiryDateGreaterThanEqualAndCustomerEmailAndPointGreaterThanOrderByExpiryDateDesc(LocalDate.now(), email, 0, pageable);
	}
	
}
