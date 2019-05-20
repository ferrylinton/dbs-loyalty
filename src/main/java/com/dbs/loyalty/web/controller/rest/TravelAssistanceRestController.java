package com.dbs.loyalty.web.controller.rest;

import static com.dbs.loyalty.config.constant.RestConstant.GET_TRAVEL_ASSISTANCE_LIMIT;
import static com.dbs.loyalty.config.constant.SwaggerConstant.JSON;
import static com.dbs.loyalty.config.constant.SwaggerConstant.JWT;
import static com.dbs.loyalty.config.constant.SwaggerConstant.OK;
import static com.dbs.loyalty.config.constant.SwaggerConstant.TRAVEL_ASSISTANCE;

import java.util.Optional;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dbs.loyalty.domain.TravelAssistance;
import com.dbs.loyalty.service.TravelAssistanceService;
import com.dbs.loyalty.service.dto.TravelAssistanceDto;
import com.dbs.loyalty.service.mapper.TravelAssistanceMapper;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.annotations.Authorization;
import lombok.RequiredArgsConstructor;

/**
 * REST controller for Travel Assistance API
 * 
 * @author Ferry L. H. <ferrylinton@gmail.com>
 * 
 */
@Api(tags = { TRAVEL_ASSISTANCE })
@RequiredArgsConstructor
@PreAuthorize("hasRole('CUSTOMER')")
@RestController
@RequestMapping("/api")
public class TravelAssistanceRestController {

	private final TravelAssistanceService travelAssistanceService;
	
	private final TravelAssistanceMapper travelAssistanceMapper;
	
	@ApiOperation(value=GET_TRAVEL_ASSISTANCE_LIMIT, produces=JSON, authorizations={@Authorization(value=JWT)})
    @ApiResponses(value={@ApiResponse(code=200, message=OK, response=TravelAssistanceDto.class)})
    @GetMapping("/travel-assistances")
    public TravelAssistanceDto getLimit(){
		Optional<TravelAssistance> travelAssistance = travelAssistanceService.findById();
		
		if(travelAssistance.isPresent()) {
    		return travelAssistanceMapper.toDto(travelAssistance.get());
    	}else {
    		return new TravelAssistanceDto(0);
    	}
    }
    
}
