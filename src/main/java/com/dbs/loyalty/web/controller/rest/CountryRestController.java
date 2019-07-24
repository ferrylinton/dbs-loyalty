package com.dbs.loyalty.web.controller.rest;

import java.util.List;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dbs.loyalty.aop.LogAuditApi;
import com.dbs.loyalty.config.constant.SwaggerConstant;
import com.dbs.loyalty.service.CountryService;
import com.dbs.loyalty.service.dto.CountryDto;
import com.dbs.loyalty.service.mapper.CountryMapper;
import com.dbs.loyalty.web.response.Response;

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
@Api(tags = { SwaggerConstant.COUNTRY })
@RequiredArgsConstructor
@PreAuthorize("hasRole('CUSTOMER')")
@RestController
@RequestMapping("/api/countries")
public class CountryRestController {
	
	private static final String GET_COUNTRIES = "GetCountires";
	
	private static final String SYNC_COUNTRIES = "SyncCountires";

    private final CountryService countryService;
    
    private final CountryMapper countryMapper;

    /**
     * GET  /api/countries : Get all countries
     *
     * @return list of countries
     */
    @ApiOperation(
    		nickname=GET_COUNTRIES, 
    		value=GET_COUNTRIES, 
    		produces=SwaggerConstant.JSON, 
    		authorizations={@Authorization(value=SwaggerConstant.JWT)})
    @ApiResponses(value = { @ApiResponse(code=200, message=SwaggerConstant.OK, response=CountryDto.class, responseContainer="List")})
    @LogAuditApi(name=GET_COUNTRIES)
    @GetMapping
    public List<CountryDto> getCountries() {
    	List<CountryDto> countries = countryMapper.toDto(countryService.findAll());
    	return countries;
    }
    
    /**
     * GET  /api/countries/sync : Sync countries data
     *
     * @return response
     */
    @ApiOperation(
    		nickname=SYNC_COUNTRIES, 
    		value=SYNC_COUNTRIES, 
    		produces=SwaggerConstant.JSON, 
    		authorizations={@Authorization(value=SwaggerConstant.JWT)})
    @ApiResponses(value = { @ApiResponse(code=200, message=SwaggerConstant.OK, response=Response.class)})
    @LogAuditApi(name=SYNC_COUNTRIES)
    @GetMapping("/sync")
    public Response sync() {
    	countryService.sync();
    	return new Response("Request is processed");
    }

}
