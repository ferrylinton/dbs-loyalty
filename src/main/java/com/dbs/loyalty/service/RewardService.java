package com.dbs.loyalty.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.dbs.loyalty.domain.Reward;
import com.dbs.loyalty.repository.RewardRepository;
import com.dbs.loyalty.service.dto.TotalDto;
import com.dbs.loyalty.util.SecurityUtil;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class RewardService {

	private final RewardRepository rewardRepository;
	
	public List<Reward> findAllValid(){
		return rewardRepository.findAllValid(SecurityUtil.getId());
	}
	
	public TotalDto getTotal(){
		Integer total = 0;
		List<Reward> rewards = rewardRepository.findAllValid(SecurityUtil.getId());
		
		for(Reward reward : rewards) {
			total += reward.getPoint();
		}

		return new TotalDto(total);
	}
	
}
