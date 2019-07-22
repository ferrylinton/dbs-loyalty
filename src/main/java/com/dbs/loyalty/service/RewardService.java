package com.dbs.loyalty.service;

import java.time.Instant;
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
	
	public int getAvailablePoints(){
		List<Reward> rewards = rewardRepository.findAllValid(SecurityUtil.getId());
		return getAvailablePoints(rewards);
	}
	
	public int getAvailablePoints(List<Reward> rewards){
		Integer total = 0;

		for(Reward reward : rewards) {
			total += reward.getPoint();
		}

		return total;
	}
	
	public void save(List<Reward> rewards) {
		rewardRepository.saveAll(rewards);
	}
	
	public void deduct(String email, List<Reward> rewards, Integer deduction) {
		for(Reward reward : rewards) {
			if(deduction > 0) {
				if(deduction > reward.getPoint()) {
					deduction = deduction - reward.getPoint();
					reward.setPoint(0);
				}else{
					reward.setPoint(reward.getPoint() - deduction);
					deduction = 0;
				}
				
				reward.setLastModifiedBy(email);
				reward.setLastModifiedDate(Instant.now());
			}
		}
		
		rewardRepository.saveAll(rewards);
	}
}
