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

import com.dbs.loyalty.config.constant.SwaggerConstant;
import com.dbs.loyalty.domain.Departure;
import com.dbs.loyalty.exception.BadRequestException;
import com.dbs.loyalty.service.DepartureService;
import com.dbs.loyalty.service.LogAuditCustomerService;
import com.dbs.loyalty.service.dto.DepartureDto;
import com.dbs.loyalty.service.mapper.DepartureMapper;
import com.dbs.loyalty.util.UrlUtil;
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

	public static final String ADD_DEPARTURE = "AddDeparture";

	private final DepartureService departureService;
	
	private final DepartureMapper departureMapper;
	
	private final LogAuditCustomerService logAuditCustomerService;

	@ApiOperation(
			value="AddDeparture", 
			produces="application/json", 
			authorizations={@Authorization(value="JWT")})
    @ApiResponses(value={ @ApiResponse(code=200, message="OK", response=Response.class)})
	@PostMapping
    public DepartureDto addDeparture(@Valid @RequestBody DepartureDto requestData, HttpServletRequest request) throws BadRequestException{
		Departure departure = departureService.save(departureMapper.toEntity(requestData));
		logAuditCustomerService.save(ADD_DEPARTURE, UrlUtil.getFullUrl(request), requestData);
		return departureMapper.toDto(departure);
    }
    
	@InitBinder
	protected void initBinder(WebDataBinder binder) {
		binder.addValidators(new DepartureDtoValidator(departureService));
	}
	
}
