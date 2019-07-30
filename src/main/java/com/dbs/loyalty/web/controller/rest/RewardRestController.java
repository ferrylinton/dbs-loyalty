package com.dbs.loyalty.web.controller.rest;

import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dbs.loyalty.aop.EnableLogAuditCustomer;
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
@RequestMapping("/api/rewards")
public class RewardRestController {
	
	public static final String GET_TOTAL_REWARDS = "GetTotalRewards";
	
	public static final String GET_ALL_REWARDS = "GetAllRewards";
	
	private final RewardService rewardService;

	private final RewardMapper rewardMapper;
	
	@ApiOperation(
			value=GET_TOTAL_REWARDS, 
			produces="application/json", 
			authorizations={@Authorization(value="JWT")})
	@ApiResponses(value={@ApiResponse(code=200, message="OK", response=TotalDto.class)})
	@EnableLogAuditCustomer(operation=GET_TOTAL_REWARDS)
	@GetMapping("/total")
	public TotalDto getTotalRewards(HttpServletRequest request, HttpServletResponse response){
		return new TotalDto(rewardService.getAvailablePoints());
	}
	
	@ApiOperation(
			value=GET_ALL_REWARDS, 
			produces="application/json", 
			authorizations={@Authorization(value="JWT")})
	@ApiResponses(value={@ApiResponse(code=200, message="OK", response=RewardDto.class)})
	@EnableLogAuditCustomer(operation=GET_ALL_REWARDS)
	@GetMapping
	public List<RewardDto> getAllRewards(HttpServletRequest request, HttpServletResponse response){
		return rewardService
				.findAllValid()
				.stream()
				.map(reward -> rewardMapper.toDto(reward))
				.collect(Collectors.toList());
	}
	
}
