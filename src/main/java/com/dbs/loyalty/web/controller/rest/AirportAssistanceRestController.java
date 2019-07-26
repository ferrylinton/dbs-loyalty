package com.dbs.loyalty.web.controller.rest;

import java.util.Optional;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dbs.loyalty.aop.LogAuditApi;
import com.dbs.loyalty.config.constant.SwaggerConstant;
import com.dbs.loyalty.domain.AirportAssistance;
import com.dbs.loyalty.service.AirportAssistanceService;
import com.dbs.loyalty.service.dto.AirportAssistanceDto;
import com.dbs.loyalty.service.mapper.AirportAssistanceMapper;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.annotations.Authorization;
import lombok.RequiredArgsConstructor;

/**
 * REST controller for Airport Assistance API
 * 
 * @author Ferry L. H. <ferrylinton@gmail.com>
 * 
 */
@Api(tags = { SwaggerConstant.AIRPORT_ASSISTANCE })
@RequiredArgsConstructor
@PreAuthorize("hasRole('CUSTOMER')")
@RestController
@RequestMapping("/api/airport-assistances")
public class AirportAssistanceRestController {
	
	public static final String GET_AIRPORT_ASSISTANCE_LIMIT = "GetAirportAssitanceLimit";

	private final AirportAssistanceService airportAssistanceService;
	
	private final AirportAssistanceMapper airportAssistanceMapper;
	
	@ApiOperation(
			value=GET_AIRPORT_ASSISTANCE_LIMIT, 
			produces="application/json", 
			authorizations={@Authorization(value="JWT")})
    @ApiResponses(value={@ApiResponse(code=200, message="OK", response=AirportAssistanceDto.class)})
	@LogAuditApi(name=GET_AIRPORT_ASSISTANCE_LIMIT)
	@GetMapping
    public AirportAssistanceDto getLimit(){
		Optional<AirportAssistance> airportAssistance = airportAssistanceService.findById();
		
		if(airportAssistance.isPresent()) {
    		return airportAssistanceMapper.toDto(airportAssistance.get());
    	}else {
    		return new AirportAssistanceDto(0);
    	}
    }
    
}
