package com.dbs.priviledge.web.controller.rest;

import java.security.Principal;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dbs.priviledge.domain.Reward;
import com.dbs.priviledge.service.RewardService;

@RestController
@RequestMapping("/api/reward")
public class RewardRestController {
	
	private final RewardService rewardService;
	
	public RewardRestController(RewardService rewardService) {
		this.rewardService = rewardService;
	}

	@GetMapping
	public Page<Reward> get(Pageable pageable, Principal principal){
		return rewardService.findByExpiryDate(principal.getName(), pageable);
	}
	
}
