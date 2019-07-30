package com.dbs.loyalty.web.controller.rest;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dbs.loyalty.config.constant.SwaggerConstant;
import com.dbs.loyalty.domain.Customer;
import com.dbs.loyalty.exception.BadRequestException;
import com.dbs.loyalty.service.CustomerService;
import com.dbs.loyalty.service.dto.CustomerActivateDto;
import com.dbs.loyalty.service.dto.CustomerDto;
import com.dbs.loyalty.service.mapper.CustomerMapper;
import com.dbs.loyalty.web.response.Response;
import com.dbs.loyalty.web.validator.CustomerActivateValidator;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.annotations.Authorization;
import lombok.RequiredArgsConstructor;

@Api(tags = { SwaggerConstant.CUSTOMER })
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/customers")
public class CustomerActivateRestController {

	public static final String ACTIVATE_CUSTOMER =  "ActivateCustomer";
	
    private final CustomerService customerService;
    
    private final CustomerMapper customerMapper;

    @ApiOperation(
    		value=ACTIVATE_CUSTOMER, 
    		produces="application/json", 
    		authorizations={@Authorization(value="JWT")})
    @ApiResponses(value={@ApiResponse(code=200, message="OK", response=Response.class)})
    @PostMapping("/activate")
    public CustomerDto activate(
    		@ApiParam(name = "CustomerActivateData", value = "Customer's activate data") 
    		@Valid @RequestBody CustomerActivateDto customerActivateDto,
    		HttpServletRequest request) throws BadRequestException  {
    	
    	Customer customer = customerService.activate(customerActivateDto);
    	return customerMapper.toDto(customer);
    }
    
    @InitBinder
	protected void initPasswordBinder(WebDataBinder binder) {
		binder.addValidators(new CustomerActivateValidator());
	}

}
