package com.dbs.priviledge.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.dbs.priviledge.domain.RewardOperation;
import com.dbs.priviledge.repository.RewardOperationRepository;

@Service
public class RewardOperationService {

	private final RewardOperationRepository rewardOperationRepository;
	
	public RewardOperationService(RewardOperationRepository rewardOperationRepository) {
		this.rewardOperationRepository = rewardOperationRepository;
	}
	
	public Page<RewardOperation> findAll(Pageable pageable){
		return rewardOperationRepository.findAll(pageable);
	}
	
}
