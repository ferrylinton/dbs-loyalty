package com.dbs.loyalty.web.controller.rest;

import static com.dbs.loyalty.config.constant.RestConstant.GET_ALL_REWARDS;
import static com.dbs.loyalty.config.constant.RestConstant.GET_TOTAL_REWARDS;
import static com.dbs.loyalty.config.constant.SwaggerConstant.JSON;
import static com.dbs.loyalty.config.constant.SwaggerConstant.JWT;
import static com.dbs.loyalty.config.constant.SwaggerConstant.OK;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dbs.loyalty.config.constant.SwaggerConstant;
import com.dbs.loyalty.service.RewardService;
import com.dbs.loyalty.service.dto.RewardDto;
import com.dbs.loyalty.service.dto.TotalDto;
import com.dbs.loyalty.service.mapper.RewardMapper;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.annotations.Authorization;
import lombok.RequiredArgsConstructor;

/**
 * REST controller for Reward API
 * 
 * @author Ferry L. H. <ferrylinton@gmail.com>
 * 
 */
@Api(tags = { SwaggerConstant.REWARD })
@RequiredArgsConstructor
@PreAuthorize("hasRole('CUSTOMER')")
@RestController
@RequestMapping("/api")
public class RewardRestController {
	
	private final RewardService rewardService;

	private final RewardMapper rewardMapper;
	
	@ApiOperation(value=GET_TOTAL_REWARDS, produces=JSON, authorizations={@Authorization(value=JWT)})
	@ApiResponses(value={@ApiResponse(code=200, message=OK, response=TotalDto.class)})
	@GetMapping("/rewards/total")
	public TotalDto getTotalRewards(){
		return rewardService.getTotal();
	}
	
	@ApiOperation(value=GET_ALL_REWARDS, produces=JSON, authorizations={@Authorization(value=JWT)})
	@ApiResponses(value={@ApiResponse(code=200, message=OK, response=RewardDto.class)})
	@GetMapping("/rewards")
	public List<RewardDto> getAllRewards(){
		return rewardService
				.findAllValid()
				.stream()
				.map(reward -> rewardMapper.toDto(reward))
				.collect(Collectors.toList());
	}
	
}
