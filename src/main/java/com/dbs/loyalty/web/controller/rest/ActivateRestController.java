package com.dbs.loyalty.web.controller.rest;

import static com.dbs.loyalty.config.constant.LogConstant.ACTIVATE_CUSTOMER;
import static com.dbs.loyalty.config.constant.SwaggerConstant.CUSTOMER;
import static com.dbs.loyalty.config.constant.SwaggerConstant.JSON;
import static com.dbs.loyalty.config.constant.SwaggerConstant.JWT;
import static com.dbs.loyalty.config.constant.SwaggerConstant.OK;

import javax.validation.Valid;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dbs.loyalty.config.constant.MessageConstant;
import com.dbs.loyalty.exception.BadRequestException;
import com.dbs.loyalty.service.CustomerService;
import com.dbs.loyalty.service.dto.CustomerActivateDto;
import com.dbs.loyalty.web.controller.AbstractController;
import com.dbs.loyalty.web.response.Response;
import com.dbs.loyalty.web.response.SuccessResponse;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.annotations.Authorization;
import lombok.RequiredArgsConstructor;

@Api(tags = { CUSTOMER })
@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
public class ActivateRestController extends AbstractController{

    private final CustomerService customerService;

    @ApiOperation(value=ACTIVATE_CUSTOMER, produces=JSON, authorizations={@Authorization(value=JWT)})
    @ApiResponses(value={@ApiResponse(code=200, message=OK, response=Response.class)})
    @PreAuthorize("hasRole('TOKEN')")
    @PostMapping("/customers/activate")
    public Response activate(
    		@ApiParam(name = "ChangePasswordData", value = "Customer's password data") 
    		@Valid @RequestBody CustomerActivateDto customerActivateDto) throws BadRequestException  {
    	
    	if(!customerActivateDto.getPassword().equals(customerActivateDto.getConfirmPassword())) {
    		throw new BadRequestException(MessageConstant.PASSWORD_IS_NOT_CONFIRMED);
    	}
    	
    	customerService.activate(customerActivateDto);
    	return new SuccessResponse(MessageConstant.CUSTOMER_IS_ACTIVATED);
    }

}
