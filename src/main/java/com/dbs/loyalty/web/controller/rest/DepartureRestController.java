package com.dbs.loyalty.web.controller.rest;

import static com.dbs.loyalty.config.constant.RestConstant.ADD_DEPARTURE;
import static com.dbs.loyalty.config.constant.SwaggerConstant.JSON;
import static com.dbs.loyalty.config.constant.SwaggerConstant.JWT;
import static com.dbs.loyalty.config.constant.SwaggerConstant.OK;
import static com.dbs.loyalty.config.constant.SwaggerConstant.TRAVEL_ASSISTANCE;

import javax.validation.Valid;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dbs.loyalty.exception.BadRequestException;
import com.dbs.loyalty.service.DepartureService;
import com.dbs.loyalty.service.dto.DepartureDto;
import com.dbs.loyalty.service.mapper.DepartureMapper;
import com.dbs.loyalty.web.response.Response;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.annotations.Authorization;
import lombok.RequiredArgsConstructor;

/**
 * REST controller for Departure API
 * 
 * @author Ferry L. H. <ferrylinton@gmail.com>
 * 
 */
@Api(tags = { TRAVEL_ASSISTANCE })
@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
public class DepartureRestController {

	private final DepartureService departureService;
	
	private final DepartureMapper departureMapper;

	@ApiOperation(value=ADD_DEPARTURE, produces=JSON, authorizations={@Authorization(value=JWT)})
    @ApiResponses(value={ @ApiResponse(code=200, message=OK, response=Response.class)})
    @PreAuthorize("hasRole('CUSTOMER')")
    @PostMapping("/departures")
    public Response addDeparture(@Valid @RequestBody DepartureDto departure) throws BadRequestException{
		return departureService.save(departureMapper.toEntity(departure));
    }
    
}
