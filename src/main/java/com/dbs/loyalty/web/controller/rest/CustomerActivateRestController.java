package com.dbs.loyalty.web.controller.rest;

import static com.dbs.loyalty.config.constant.RestConstant.ACTIVATE_CUSTOMER;
import static com.dbs.loyalty.config.constant.RestConstant.FORGOT_PASSWORD;
import static com.dbs.loyalty.config.constant.SwaggerConstant.CUSTOMER;
import static com.dbs.loyalty.config.constant.SwaggerConstant.JSON;
import static com.dbs.loyalty.config.constant.SwaggerConstant.JWT;
import static com.dbs.loyalty.config.constant.SwaggerConstant.OK;

import javax.validation.Valid;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.dbs.loyalty.config.constant.MessageConstant;
import com.dbs.loyalty.exception.BadRequestException;
import com.dbs.loyalty.service.CustomerService;
import com.dbs.loyalty.service.dto.CustomerNewPasswordDto;
import com.dbs.loyalty.web.controller.AbstractController;
import com.dbs.loyalty.web.response.Response;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.annotations.Authorization;
import lombok.RequiredArgsConstructor;

@Api(tags = { CUSTOMER })
@RequiredArgsConstructor
@PreAuthorize("hasRole('TOKEN')")
@RestController
public class CustomerActivateRestController extends AbstractController{

    private final CustomerService customerService;

    @ApiOperation(value=ACTIVATE_CUSTOMER, produces=JSON, authorizations={@Authorization(value=JWT)})
    @ApiResponses(value={@ApiResponse(code=200, message=OK, response=Response.class)})
    @PutMapping("/api/customers/activate")
    public Response activate(
    		@ApiParam(name = "CustomerNewPassword", value = "Customer's new password") 
    		@Valid @RequestBody CustomerNewPasswordDto customerNewPasswordDto) throws BadRequestException  {
    	return saveNewPassword(customerNewPasswordDto);
    }
    
    @ApiOperation(value=FORGOT_PASSWORD, produces=JSON, authorizations={@Authorization(value=JWT)})
    @ApiResponses(value={@ApiResponse(code=200, message=OK, response=Response.class)})
    @PutMapping("/api/customers/forgot")
    public Response forgot(
    		@ApiParam(name = "CustomerNewPassword", value = "Customer's new password") 
    		@Valid @RequestBody CustomerNewPasswordDto customerNewPasswordDto) throws BadRequestException  {
    	return saveNewPassword(customerNewPasswordDto);
    }
    
    private Response saveNewPassword(CustomerNewPasswordDto customerNewPasswordDto) throws BadRequestException  {
    	
    	if(!customerNewPasswordDto.getPassword().equals(customerNewPasswordDto.getConfirmPassword())) {
    		throw new BadRequestException(MessageConstant.PASSWORD_IS_NOT_CONFIRMED);
    	}
    	
    	customerService.activate(customerNewPasswordDto);
    	return new Response(MessageConstant.CUSTOMER_IS_ACTIVATED);
    }

}
