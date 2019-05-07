package com.dbs.loyalty.web.controller.rest;

import static com.dbs.loyalty.config.constant.SwaggerConstant.JWT;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.MediaType;
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

@Api(tags = { SwaggerConstant.REWARD })
@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
public class RewardRestController {
	
	private final RewardService rewardService;

	private final RewardMapper rewardMapper;
	
	@ApiOperation(
			nickname="GetTotalReward", 
			value="GetTotalReward", 
			notes="Get Total Reward",
    		produces=MediaType.APPLICATION_JSON_VALUE, 
    		authorizations = { @Authorization(value=JWT) })
    @ApiResponses(value={@ApiResponse(code=200, message="OK", response = TotalDto.class)})
	@PreAuthorize("hasRole('CUSTOMER')")
	@GetMapping("/rewards/total")
	public TotalDto getTotal(){
		return rewardService.getTotal();
	}
	
	@ApiOperation(
			nickname="GetAllReward", 
			value="GetAllReward", 
			notes="Get All Reward",
    		produces=MediaType.APPLICATION_JSON_VALUE, 
    		authorizations = { @Authorization(value=JWT) })
    @ApiResponses(value={@ApiResponse(code=200, message="OK", response = RewardDto.class)})
	@PreAuthorize("hasRole('CUSTOMER')")
	@GetMapping("/rewards")
	public List<RewardDto> getAllReward(){
		return rewardService
				.findAllValid()
				.stream()
				.map(reward -> rewardMapper.toDto(reward))
				.collect(Collectors.toList());
	}
	
}
