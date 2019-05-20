package com.dbs.loyalty.web.controller.rest;

import static com.dbs.loyalty.config.constant.RestConstant.GET_AIRPORTS;
import static com.dbs.loyalty.config.constant.SwaggerConstant.JSON;
import static com.dbs.loyalty.config.constant.SwaggerConstant.JWT;
import static com.dbs.loyalty.config.constant.SwaggerConstant.OK;
import static com.dbs.loyalty.config.constant.SwaggerConstant.TRAVEL_ASSISTANCE;

import java.util.Collections;
import java.util.List;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
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
@PreAuthorize("hasRole('CUSTOMER')")
@RestController
public class AirportRestController {

    private final CountryService countryService;
    
    private final CountryMapper countryMapper;

    /**
     * GET  /api/airports : Get all airports
     *
     * @return list of airports
     */
    @ApiOperation(nickname=GET_AIRPORTS, value=GET_AIRPORTS, produces=JSON, authorizations={@Authorization(value=JWT)})
    @ApiResponses(value = { @ApiResponse(code=200, message=OK, response=CountryDto.class, responseContainer="List")})
    @GetMapping("/api/airports")
    public List<CountryDto> getAirports() {
    	List<CountryDto> countries = countryMapper.toDto(countryService.findAll());
    	
    	for(CountryDto country : countries) {
    		Collections.sort(country.getAirports());
    	}
    	
    	return countries;
    }

}
