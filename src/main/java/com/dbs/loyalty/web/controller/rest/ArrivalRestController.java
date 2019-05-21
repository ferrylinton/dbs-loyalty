package com.dbs.loyalty.web.controller.rest;

import static com.dbs.loyalty.config.constant.RestConstant.ADD_ARRIVAL;
import static com.dbs.loyalty.config.constant.SwaggerConstant.JSON;
import static com.dbs.loyalty.config.constant.SwaggerConstant.JWT;
import static com.dbs.loyalty.config.constant.SwaggerConstant.OK;

import java.beans.PropertyEditorSupport;
import java.time.Instant;
import java.time.format.DateTimeFormatter;

import static com.dbs.loyalty.config.constant.SwaggerConstant.AIRPORT_ASSISTANCE;

import javax.validation.Valid;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.dbs.loyalty.exception.BadRequestException;
import com.dbs.loyalty.service.ArrivalService;
import com.dbs.loyalty.service.dto.ArrivalDto;
import com.dbs.loyalty.service.mapper.ArrivalMapper;
import com.dbs.loyalty.web.response.Response;

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
@Api(tags = { AIRPORT_ASSISTANCE })
@RequiredArgsConstructor
@PreAuthorize("hasRole('CUSTOMER')")
@RestController
public class ArrivalRestController {

	private final ArrivalService arrivalService;
	
	private final ArrivalMapper arrivalMapper;

    @ApiOperation(value=ADD_ARRIVAL, produces=JSON, authorizations={@Authorization(value=JWT)})
    @ApiResponses(value={@ApiResponse(code=200, message=OK, response=Response.class)})
    @PostMapping("/api/arrivals")
    public Response addArrival(@Valid @RequestBody ArrivalDto arrivalDto, BindingResult result) throws BadRequestException{
    	return arrivalService.save(arrivalMapper.toEntity(arrivalDto));
    }
    
    @InitBinder
	protected void initBinder(WebDataBinder binder) {
		binder.registerCustomEditor(Instant.class, new PropertyEditorSupport() {
			
		    @Override
		    public void setAsText(String text) throws IllegalArgumentException{
		      setValue(Instant.parse(text));
		    }

		    @Override
		    public String getAsText() throws IllegalArgumentException {
		      return DateTimeFormatter.ISO_INSTANT.format((Instant) getValue());
		    }
		    
		});
	}
    
}
