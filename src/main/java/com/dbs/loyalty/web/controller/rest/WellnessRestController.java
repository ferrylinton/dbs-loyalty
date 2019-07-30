package com.dbs.loyalty.web.controller.rest;

import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dbs.loyalty.aop.EnableLogAuditCustomer;
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
			produces="application/json", 
			authorizations={@Authorization(value="JWT")})
	@ApiResponses(value={@ApiResponse(code=200, message="OK", response=WellnessDto.class)})
	@EnableLogAuditCustomer(operation=GET_WELLNESS_LIMIT)
	@GetMapping
    public WellnessDto getLimit(HttpServletRequest request, HttpServletResponse response) {
    	Optional<Wellness> wellness = wellnessService.findById();
    	
    	if(wellness.isPresent()) {
    		return wellnessMapper.toDto(wellness.get());
    	}else {
    		return new WellnessDto(0);
    	}
    }
    
}
