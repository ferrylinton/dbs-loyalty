package com.dbs.loyalty.web.controller.rest;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dbs.loyalty.aop.EnableLogAuditCustomer;
import com.dbs.loyalty.config.constant.SwaggerConstant;
import com.dbs.loyalty.domain.Arrival;
import com.dbs.loyalty.exception.BadRequestException;
import com.dbs.loyalty.service.ArrivalService;
import com.dbs.loyalty.service.dto.ArrivalDto;
import com.dbs.loyalty.service.mapper.ArrivalMapper;
import com.dbs.loyalty.web.response.Response;
import com.dbs.loyalty.web.validator.rest.ArrivalDtoValidator;

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
@Api(tags = { SwaggerConstant.AIRPORT_ASSISTANCE })
@RequiredArgsConstructor
@PreAuthorize("hasRole('CUSTOMER')")
@RestController
@RequestMapping("/api/arrivals")
public class ArrivalRestController {
	
	public static final String ADD_ARRIVAL = "AddArrival";

	private final ArrivalService arrivalService;
	
	private final ArrivalMapper arrivalMapper;
	
    @ApiOperation(
    		value=ADD_ARRIVAL, 
    		produces="application/json", 
    		authorizations={@Authorization(value="JWT")})
    @ApiResponses(value={@ApiResponse(code=200, message="OK", response=Response.class)})
    @EnableLogAuditCustomer(operation=ADD_ARRIVAL)
    @PostMapping
    public ArrivalDto addArrival(@Valid @RequestBody ArrivalDto requestData, HttpServletRequest request) throws BadRequestException{
    	Arrival arrival = arrivalService.save(arrivalMapper.toEntity(requestData));
		return arrivalMapper.toDto(arrival);
    }
    
    @InitBinder
	protected void initBinder(WebDataBinder binder) {
		binder.addValidators(new ArrivalDtoValidator(arrivalService));
	}
    
}
