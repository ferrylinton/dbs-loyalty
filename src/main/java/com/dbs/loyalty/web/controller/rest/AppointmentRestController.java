package com.dbs.loyalty.web.controller.rest;

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
import com.dbs.loyalty.exception.BadRequestException;
import com.dbs.loyalty.exception.NotFoundException;
import com.dbs.loyalty.service.AppointmentService;
import com.dbs.loyalty.service.dto.AppointmentDto;
import com.dbs.loyalty.service.mapper.AppointmentMapper;
import com.dbs.loyalty.web.response.Response;
import com.dbs.loyalty.web.validator.rest.AppointmentDtoValidator;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.annotations.Authorization;
import lombok.RequiredArgsConstructor;

/**
 * REST controller for Appointment API
 * 
 * @author Ferry L. H. <ferrylinton@gmail.com>
 * 
 */
@Api(tags = { SwaggerConstant.WELLNESS })
@RequiredArgsConstructor
@PreAuthorize("hasRole('CUSTOMER')")
@RestController
@RequestMapping("/api/appointments")
public class AppointmentRestController {
	
	public static final String ADD_APPOINTMENT = "AddAppointment";
	
	public static final String BINDER_NAME = "appointmentDto";

	private final AppointmentService appointmentService;
	
	private final AppointmentMapper appointmentMapper;

	@ApiOperation(
			nickname=ADD_APPOINTMENT, 
			value=ADD_APPOINTMENT, 
			produces="application/json", 
			authorizations={@Authorization(value="JWT")})
	@ApiResponses(value={@ApiResponse(code=200, message="OK", response=Response.class)})
	@EnableLogAuditCustomer(operation=ADD_APPOINTMENT)
	@PostMapping
    public Response addAppointment(@Valid @RequestBody AppointmentDto appointmentDto) throws BadRequestException, NotFoundException{
    	return appointmentService.save(appointmentMapper.toEntity(appointmentDto));
    }
    
	@InitBinder(BINDER_NAME)
	protected void initBinder(WebDataBinder binder) {
		binder.addValidators(new AppointmentDtoValidator(appointmentService));
	}
	
}
