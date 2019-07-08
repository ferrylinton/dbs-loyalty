package com.dbs.loyalty.web.controller.rest;

import javax.validation.Valid;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dbs.loyalty.config.constant.RestConstant;
import com.dbs.loyalty.config.constant.SwaggerConstant;
import com.dbs.loyalty.exception.BadRequestException;
import com.dbs.loyalty.service.DepartureService;
import com.dbs.loyalty.service.dto.DepartureDto;
import com.dbs.loyalty.service.mapper.DepartureMapper;
import com.dbs.loyalty.web.response.Response;
import com.dbs.loyalty.web.validator.rest.DepartureDtoValidator;

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
@Api(tags = { SwaggerConstant.AIRPORT_ASSISTANCE })
@RequiredArgsConstructor
@PreAuthorize("hasRole('CUSTOMER')")
@RestController
@RequestMapping("/api/departures")
public class DepartureRestController {

	private final DepartureService departureService;
	
	private final DepartureMapper departureMapper;

	@ApiOperation(
			value=RestConstant.ADD_DEPARTURE, 
			produces=SwaggerConstant.JSON, 
			authorizations={@Authorization(value=SwaggerConstant.JWT)})
    @ApiResponses(value={ @ApiResponse(code=200, message=SwaggerConstant.OK, response=Response.class)})
    @PostMapping
    public Response addDeparture(@Valid @RequestBody DepartureDto departureDto) throws BadRequestException{
		return departureService.save(departureMapper.toEntity(departureDto));
    }
    
	@InitBinder("departureDto")
	protected void initBinder(WebDataBinder binder) {
		binder.addValidators(new DepartureDtoValidator(departureService));
	}
	
}
