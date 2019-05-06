package com.dbs.loyalty.web.controller.rest;

import static com.dbs.loyalty.config.constant.SwaggerConstant.JWT;
import static com.dbs.loyalty.config.constant.SwaggerConstant.TRAVEL_ASSISTANCE;

import java.util.Optional;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dbs.loyalty.domain.TravelAssistance;
import com.dbs.loyalty.exception.BadRequestException;
import com.dbs.loyalty.exception.NotFoundException;
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
 * REST controller for Travel Assistance
 * 
 * @author Ferry L. H. <ferrylinton@gmail.com>
 * 
 */
@Api(tags = { TRAVEL_ASSISTANCE })
@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
public class TravelAssistanceRestController {

	private final TravelAssistanceService travelAssistanceService;
	
	private final TravelAssistanceMapper travelAssistanceMapper;

    @ApiOperation(
    		nickname		= "GetTravelAssitanceLimit", 
    		value			= "GetTravelAssitanceLimit", 
    		notes			= "Get Travel Assitance Limit",
    		produces		= MediaType.APPLICATION_JSON_VALUE, 
    		authorizations	= { @Authorization(value=JWT) })
    @ApiResponses(value={@ApiResponse(code=200, message="OK", response = TravelAssistanceDto.class)})
    @PreAuthorize("hasRole('CUSTOMER')")
    @GetMapping("/travel-assistances")
    public ResponseEntity<TravelAssistanceDto> getLimit() throws NotFoundException, BadRequestException{
    	Optional<TravelAssistance> travelAssistance = travelAssistanceService.findById();
    	
    	if(travelAssistance.isPresent()) {
    		return ResponseEntity.ok().body(travelAssistanceMapper.toDto(travelAssistance.get()));
    	}else {
    		return ResponseEntity.ok().body(new TravelAssistanceDto(0));
    	}
    }
    
}
