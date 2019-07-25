package com.dbs.loyalty.web.controller.rest;

import java.util.Optional;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dbs.loyalty.aop.LogAuditApi;
import com.dbs.loyalty.config.constant.SwaggerConstant;
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
@Api(tags = { SwaggerConstant.WELLNESS })
@RequiredArgsConstructor
@PreAuthorize("hasRole('CUSTOMER')")
@RestController
@RequestMapping("/api/wellness")
public class WellnessRestController {
	
	public static final String GET_WELLNESS_LIMIT = "GetWellnessLimit";

	private final WellnessService wellnessService;
	
	private final WellnessMapper wellnessMapper;

	@ApiOperation(
			value=GET_WELLNESS_LIMIT, 
			produces=SwaggerConstant.JSON, 
			authorizations={@Authorization(value=SwaggerConstant.JWT)})
	@ApiResponses(value={@ApiResponse(code=200, message=SwaggerConstant.OK, response=WellnessDto.class)})
	@LogAuditApi(name=GET_WELLNESS_LIMIT)
	@GetMapping
    public WellnessDto getLimit() {
    	Optional<Wellness> wellness = wellnessService.findById();
    	
    	if(wellness.isPresent()) {
    		return wellnessMapper.toDto(wellness.get());
    	}else {
    		return new WellnessDto(0);
    	}
    }
    
}
