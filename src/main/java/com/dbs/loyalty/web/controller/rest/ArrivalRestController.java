package com.dbs.loyalty.web.controller.rest;

import static com.dbs.loyalty.config.constant.SwaggerConstant.JWT;
import static com.dbs.loyalty.config.constant.SwaggerConstant.TRAVEL_ASSISTANCE;

import javax.validation.Valid;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dbs.loyalty.exception.BadRequestException;
import com.dbs.loyalty.exception.NotFoundException;
import com.dbs.loyalty.service.ArrivalService;
import com.dbs.loyalty.service.dto.ArrivalDto;
import com.dbs.loyalty.service.mapper.ArrivalMapper;
import com.dbs.loyalty.web.response.Response;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.annotations.Authorization;
import lombok.RequiredArgsConstructor;

/**
 * REST controller for Arrival API
 * 
 * @author Ferry L. H. <ferrylinton@gmail.com>
 * 
 */
@Api(tags = { TRAVEL_ASSISTANCE })
@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
public class ArrivalRestController {

	private final ArrivalService arrivalService;
	
	private final ArrivalMapper arrivalMapper;

    @ApiOperation(
    		nickname		= "AddArrival", 
    		value			= "AddArrival", 
    		notes			= "Add Arrival",
    		produces		= MediaType.APPLICATION_JSON_VALUE, 
    		authorizations	= { @Authorization(value=JWT) })
    @ApiResponses(value={@ApiResponse(code=200, message="OK", response = Response.class)})
    @PreAuthorize("hasRole('CUSTOMER')")
    @PostMapping("/arrivals")
    public ResponseEntity<Response> addArrival(
    		@Valid @RequestBody ArrivalDto arrivalDto) throws NotFoundException, BadRequestException{
    	
    	Response response = arrivalService.save(arrivalMapper.toEntity(arrivalDto));
    	return ResponseEntity.ok().body(response);
    }
    
}
