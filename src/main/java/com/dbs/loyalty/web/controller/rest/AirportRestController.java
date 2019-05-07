package com.dbs.loyalty.web.controller.rest;

import static com.dbs.loyalty.config.constant.SwaggerConstant.JWT;
import static com.dbs.loyalty.config.constant.SwaggerConstant.TRAVEL_ASSISTANCE;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
@Api(tags = { TRAVEL_ASSISTANCE })
@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
public class AirportRestController {

    private final CountryService countryService;
    
    private final CountryMapper countryMapper;

    /**
     * GET  /api/airports : get all airports
     *
     * @return list of airports
     */
    @ApiOperation(
    		nickname		= "GetAirports", 
    		value			= "GetAirports", 
    		notes			= "Get all Airports",
    		produces		= MediaType.APPLICATION_JSON_VALUE, 
    		authorizations	= { @Authorization(value = JWT) })
    @ApiResponses(value = { @ApiResponse(code = 200, message="OK", response = CountryDto.class, responseContainer = "List")})
    @PreAuthorize("hasRole('CUSTOMER')")
    @GetMapping("/airports")
    public List<CountryDto> getAirports() {
    	List<CountryDto> countries = countryService
    			.findAll()
    			.stream()
				.map(country -> countryMapper.toDto(country))
				.collect(Collectors.toList());
    	
    	for(CountryDto country : countries) {
    		Collections.sort(country.getAirports());
    	}
    	
    	return countries;
    }

}
