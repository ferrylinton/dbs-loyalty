package com.dbs.loyalty.web.controller.rest;

import java.util.Collections;
import java.util.List;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dbs.loyalty.aop.EnableLogAuditCustomer;
import com.dbs.loyalty.config.constant.SwaggerConstant;
import com.dbs.loyalty.service.CountryService;
import com.dbs.loyalty.service.dto.CountryDto;
import com.dbs.loyalty.service.mapper.CountryMapper;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.annotations.Authorization;
import lombok.RequiredArgsConstructor;

/**
 * REST controller for Airport API
 * 
 * @author Ferry L. H. <ferrylinton@gmail.com>
 */
@Api(tags = { SwaggerConstant.AIRPORT_ASSISTANCE })
@RequiredArgsConstructor
@PreAuthorize("hasRole('CUSTOMER')")
@RestController
@RequestMapping("/api/airports")
public class AirportRestController {
	
	public static final String GET_AIRPORTS = "GetAirports";

    private final CountryService countryService;
    
    private final CountryMapper countryMapper;

    /**
     * GET  /api/airports : Get all airports
     *
     * @return list of airports
     */
    @ApiOperation(
    		nickname=GET_AIRPORTS, 
    		value=GET_AIRPORTS, 
    		produces="application/json", 
    		authorizations={@Authorization(value="JWT")})
    @ApiResponses(value = { @ApiResponse(code=200, message="OK", response=CountryDto.class, responseContainer="List")})
    @EnableLogAuditCustomer(operation=GET_AIRPORTS)
    @GetMapping
    public List<CountryDto> getAirports() {
    	List<CountryDto> countries = countryMapper.toDtoWithAirports(countryService.findAll());
    	
    	for(CountryDto country : countries) {
    		if(country.getAirports() != null) {
    			Collections.sort(country.getAirports());
    		}
    	}
    	
    	return countries;
    }

}
