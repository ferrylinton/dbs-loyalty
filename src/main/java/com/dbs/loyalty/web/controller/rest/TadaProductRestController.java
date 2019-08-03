package com.dbs.loyalty.web.controller.rest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
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
@Api(tags = { SwaggerConstant.PRODUCT })
@RequiredArgsConstructor
@PreAuthorize("hasRole('CUSTOMER')")
@RestController
@RequestMapping("/api")
public class TadaProductRestController {

	public static final String GET_TADA_ITEMS = "GetTadaItems";
	
	public static final String GET_TADA_CATEGORIES = "GetTadaCategories";

	private final TadaService tadaService;
	
	@ApiOperation(
		nickname = GET_TADA_ITEMS, 
		value = GET_TADA_ITEMS, 
		produces = "application/json", 
		authorizations = { @Authorization(value = "JWT") })
	@ApiResponses(value = { @ApiResponse(code = 200, message = "OK", response = String.class) })
	@EnableLogAuditCustomer(operation=GET_TADA_ITEMS)
	@GetMapping("/tada-items")
	public ResponseEntity<String> getItems(HttpServletRequest request, HttpServletResponse response) {
		return tadaService.getItems(request.getQueryString());
	}
	
	@ApiOperation(
		nickname = GET_TADA_CATEGORIES, 
		value = GET_TADA_CATEGORIES, 
		produces = "application/json", 
		authorizations = { @Authorization(value = "JWT") })
	@ApiResponses(value = { @ApiResponse(code = 200, message = "OK", response = String.class) })
	@EnableLogAuditCustomer(operation=GET_TADA_CATEGORIES)
	@GetMapping("/tada-categories")
	public ResponseEntity<String> getCategories(HttpServletRequest request, HttpServletResponse response) {
		return tadaService.getCategories(request.getQueryString());
	}

}
