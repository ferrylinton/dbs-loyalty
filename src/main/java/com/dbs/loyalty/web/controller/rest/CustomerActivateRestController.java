package com.dbs.loyalty.web.controller.rest;

import javax.validation.Valid;

import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dbs.loyalty.aop.LogAuditApi;
import com.dbs.loyalty.config.constant.MessageConstant;
import com.dbs.loyalty.config.constant.SwaggerConstant;
import com.dbs.loyalty.exception.BadRequestException;
import com.dbs.loyalty.service.CustomerService;
import com.dbs.loyalty.service.dto.CustomerActivateDto;
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
	
	public static final String BINDER_NAME = "customerActivateDto";
	
    private final CustomerService customerService;

    @ApiOperation(
    		value=ACTIVATE_CUSTOMER, 
    		produces="application/json", 
    		authorizations={@Authorization(value="JWT")})
    @ApiResponses(value={@ApiResponse(code=200, message="OK", response=Response.class)})
    @LogAuditApi(name=ACTIVATE_CUSTOMER, saveRequest=true, saveResponse=true)
    @PostMapping("/activate")
    public Response activate(
    		@ApiParam(name = "CustomerActivateData", value = "Customer's activate data") 
    		@Valid @RequestBody CustomerActivateDto customerActivateDto) throws BadRequestException  {
    	
    	customerService.activate(customerActivateDto);
    	return new Response(MessageConstant.CUSTOMER_IS_ACTIVATED);
    }
    
    @InitBinder(BINDER_NAME)
	protected void initPasswordBinder(WebDataBinder binder) {
		binder.addValidators(new CustomerActivateValidator());
	}

}
