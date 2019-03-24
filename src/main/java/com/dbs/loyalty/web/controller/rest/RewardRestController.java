package com.dbs.loyalty.web.controller.rest;

import java.security.Principal;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dbs.loyalty.config.constant.SwaggerConstant;
import com.dbs.loyalty.domain.Reward;
import com.dbs.loyalty.service.RewardService;
import com.dbs.loyalty.web.swagger.ApiPageable;

import io.swagger.annotations.Api;

@Api(tags = { SwaggerConstant.Reward })
@RestController
@RequestMapping("/api/reward")
public class RewardRestController {
	
	private final RewardService rewardService;
	
	public RewardRestController(RewardService rewardService) {
		this.rewardService = rewardService;
	}

	@GetMapping
	@ApiPageable
	public Page<Reward> get(Pageable pageable, Principal principal){
		return rewardService.findByExpiryDate(principal.getName(), pageable);
	}
	
}
