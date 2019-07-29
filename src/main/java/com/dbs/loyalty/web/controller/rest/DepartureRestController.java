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
	
	public static final String BINDER_NAME = "departureDto";
	
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
    public Response addDeparture(@Valid @RequestBody DepartureDto departureDto, HttpServletRequest request) throws Throwable{
		String url = UrlUtil.getFullUrl(request);
    	
    	try {
    		Response response = departureService.save(departureMapper.toEntity(departureDto));
    		logAuditCustomerService.save(ADD_DEPARTURE, url, departureDto);
    		return response;
		} catch (Throwable t) {
			logAuditCustomerService.saveError(ADD_DEPARTURE, url, departureDto, t);
			throw t;
		}
    }
    
	@InitBinder(BINDER_NAME)
	protected void initBinder(WebDataBinder binder) {
		binder.addValidators(new DepartureDtoValidator(departureService));
	}
	
}
