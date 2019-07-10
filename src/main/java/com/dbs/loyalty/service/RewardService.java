package com.dbs.loyalty.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.dbs.loyalty.domain.Reward;
import com.dbs.loyalty.repository.RewardRepository;
import com.dbs.loyalty.util.SecurityUtil;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class RewardService {

	private final RewardRepository rewardRepository;
	
	public List<Reward> findAllValid(){
		return rewardRepository.findAllValid(SecurityUtil.getId());
	}
	
	public int getTotal(){
		List<Reward> rewards = rewardRepository.findAllValid(SecurityUtil.getId());
		return getTotal(rewards);
	}
	
	public int getTotal(List<Reward> rewards){
		Integer total = 0;

		for(Reward reward : rewards) {
			total += reward.getPoint();
		}

		return total;
	}
	
	public void save(List<Reward> rewards) {
		rewardRepository.saveAll(rewards);
	}
	
}
