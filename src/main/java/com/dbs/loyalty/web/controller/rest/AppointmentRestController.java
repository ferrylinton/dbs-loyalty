package com.dbs.loyalty.web.controller.rest;

import static com.dbs.loyalty.config.constant.RestConstant.ADD_APPOINTMENT;
import static com.dbs.loyalty.config.constant.SwaggerConstant.JSON;
import static com.dbs.loyalty.config.constant.SwaggerConstant.JWT;
import static com.dbs.loyalty.config.constant.SwaggerConstant.OK;
import static com.dbs.loyalty.config.constant.SwaggerConstant.WELLNESS;

import javax.validation.Valid;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.dbs.loyalty.exception.BadRequestException;
import com.dbs.loyalty.exception.NotFoundException;
import com.dbs.loyalty.service.AppointmentService;
import com.dbs.loyalty.service.dto.AppointmentDto;
import com.dbs.loyalty.service.mapper.AppointmentMapper;
import com.dbs.loyalty.web.response.Response;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.annotations.Authorization;
import lombok.RequiredArgsConstructor;

/**
 * REST controller for Appointment
 * 
 * @author Ferry L. H. <ferrylinton@gmail.com>
 * 
 */
@Api(tags = { WELLNESS })
@RequiredArgsConstructor
@PreAuthorize("hasRole('CUSTOMER')")
@RestController
public class AppointmentRestController {

	private final AppointmentService appointmentService;
	
	private final AppointmentMapper appointmentMapper;

	@ApiOperation(nickname=ADD_APPOINTMENT, value=ADD_APPOINTMENT, produces=JSON, authorizations={@Authorization(value=JWT)})
	@ApiResponses(value={@ApiResponse(code=200, message=OK, response=Response.class)})
    @PostMapping("/api/appointments")
    public Response addAppointment(@Valid @RequestBody AppointmentDto appointmentDto) throws BadRequestException, NotFoundException{
    	return appointmentService.save(appointmentMapper.toEntity(appointmentDto));
    }
    
}
