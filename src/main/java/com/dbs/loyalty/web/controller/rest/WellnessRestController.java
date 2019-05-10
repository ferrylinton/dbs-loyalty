package com.dbs.loyalty.web.controller.rest;

import static com.dbs.loyalty.config.constant.LogConstant.GET_WELLNESS_LIMIT;
import static com.dbs.loyalty.config.constant.SwaggerConstant.JSON;
import static com.dbs.loyalty.config.constant.SwaggerConstant.JWT;
import static com.dbs.loyalty.config.constant.SwaggerConstant.OK;
import static com.dbs.loyalty.config.constant.SwaggerConstant.WELLNESS;

import java.util.Optional;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dbs.loyalty.domain.Wellness;
import com.dbs.loyalty.service.WellnessService;
import com.dbs.loyalty.service.dto.WellnessDto;
import com.dbs.loyalty.service.mapper.WellnessMapper;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.annotations.Authorization;
import lombok.RequiredArgsConstructor;

/**
 * REST controller for Wellness API
 * 
 * @author Ferry L. H. <ferrylinton@gmail.com>
 * 
 */
@Api(tags = { WELLNESS })
@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
public class WellnessRestController {

	private final WellnessService wellnessService;
	
	private final WellnessMapper wellnessMapper;

	@ApiOperation(value=GET_WELLNESS_LIMIT, produces=JSON, authorizations={@Authorization(value=JWT)})
	@ApiResponses(value={@ApiResponse(code=200, message=OK, response=WellnessDto.class)})
    @PreAuthorize("hasRole('CUSTOMER')")
    @GetMapping("/wellness")
    public WellnessDto getLimit() {
    	Optional<Wellness> wellness = wellnessService.findById();
    	
    	if(wellness.isPresent()) {
    		return wellnessMapper.toDto(wellness.get());
    	}else {
    		return new WellnessDto(0);
    	}
    }
    
}
