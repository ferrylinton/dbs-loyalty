package com.dbs.loyalty.web.controller.rest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dbs.loyalty.aop.EnableLogAuditCustomer;
import com.dbs.loyalty.config.constant.SwaggerConstant;
import com.dbs.loyalty.service.TadaService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.annotations.Authorization;
import lombok.RequiredArgsConstructor;

/**
 * REST controller for TADA API
 * 
 * @author Ferry L. H. <ferrylinton@gmail.com>
 */
@Api(tags = { SwaggerConstant.TADA })
@RequiredArgsConstructor
@PreAuthorize("hasRole('CUSTOMER')")
@RestController
@RequestMapping("/api")
public class TadaGeneralRestController {

	public static final String GET_TADA_COUNTRIES = "GetTadaCountries";
	
	public static final String GET_TADA_PROVINCES = "GetTadaProvinces";
	
	public static final String GET_TADA_CITIES = "GetTadaCities";

	private final TadaService tadaService;
	
	@ApiOperation(
		nickname = GET_TADA_COUNTRIES, 
		value = GET_TADA_COUNTRIES, 
		produces = "application/json", 
		authorizations = { @Authorization(value = "JWT") })
	@ApiResponses(value = { @ApiResponse(code = 200, message = "OK", response = String.class) })
	@EnableLogAuditCustomer(operation=GET_TADA_COUNTRIES)
	@GetMapping("/tada-countries")
	public ResponseEntity<String> getCountries(HttpServletRequest request, HttpServletResponse response) {
		return tadaService.getCountries();
	}
	
	@ApiOperation(
		nickname = GET_TADA_PROVINCES, 
		value = GET_TADA_PROVINCES, 
		produces = "application/json", 
		authorizations = { @Authorization(value = "JWT") })
	@ApiResponses(value = { @ApiResponse(code = 200, message = "OK", response = String.class) })
	@EnableLogAuditCustomer(operation=GET_TADA_PROVINCES)
	@GetMapping("/tada-provinces/{countryId}")
	public ResponseEntity<String> getProvinces(
			@PathVariable Integer countryId, 
			HttpServletRequest request, 
			HttpServletResponse response) {
		
		return tadaService.getProvinces(countryId);
	}
	
	@ApiOperation(
		nickname = GET_TADA_CITIES, 
		value = GET_TADA_CITIES, 
		produces = "application/json", 
		authorizations = { @Authorization(value = "JWT") })
	@ApiResponses(value = { @ApiResponse(code = 200, message = "OK", response = String.class) })
	@EnableLogAuditCustomer(operation=GET_TADA_CITIES)
	@GetMapping("/tada-cites/{countryId}/{provinceId}")
	public ResponseEntity<String> getCities(
			@PathVariable Integer countryId, 
			@PathVariable Integer provinceId, 
			HttpServletRequest request, 
			HttpServletResponse response) {
		
			return tadaService.getCities(countryId, provinceId);
	}

}
