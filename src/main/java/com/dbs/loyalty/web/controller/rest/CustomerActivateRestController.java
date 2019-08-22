package com.dbs.loyalty.web.controller.rest;

import java.util.Collections;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.dbs.loyalty.aop.EnableLogAuditCustomer;
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
import lombok.RequiredArgsConstructor;

@Api(tags = { SwaggerConstant.CUSTOMER })
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/customers")
public class CustomerActivateRestController {

	public static final String ACTIVATE_CUSTOMER =  "ActivateCustomer";
	
	public static final String CHECK_EMAIL =  "CheckEmail";
	
    private final CustomerService customerService;
    
    private final CustomerMapper customerMapper;

    @ApiOperation(
    		value=CHECK_EMAIL, 
    		produces="application/json")
    @ApiResponses(value={@ApiResponse(code=200, message="OK", response=Boolean.class, responseContainer="Map")})
    @EnableLogAuditCustomer(operation=CHECK_EMAIL)
    @GetMapping("/check-email")
    public Map<String, Boolean> checkEmail(
    		@ApiParam(name = "email", value = "Customer's email", required=true) 
    		@RequestParam(name="email", required=true) String email,
    		HttpServletRequest request, HttpServletResponse response){

    	return Collections.singletonMap("exist", customerService.isEmailExist(email));
    }
    
    @ApiOperation(
    		value=ACTIVATE_CUSTOMER, 
    		produces="application/json")
    @ApiResponses(value={@ApiResponse(code=200, message="OK", response=Response.class)})
    @EnableLogAuditCustomer(operation=ACTIVATE_CUSTOMER)
    @PostMapping("/activate")
    public CustomerDto activate(
    		@ApiParam(name = "CustomerActivateData", value = "Customer's activate data") 
    		@Valid @RequestBody CustomerActivateDto customerActivateDto,
    		HttpServletRequest request, HttpServletResponse response) throws BadRequestException  {
    	
    	Customer customer = customerService.activate(customerActivateDto);
    	return customerMapper.toDto(customer);
    }
    
    @InitBinder
	protected void initPasswordBinder(WebDataBinder binder) {
		binder.addValidators(new CustomerActivateValidator());
	}

}
