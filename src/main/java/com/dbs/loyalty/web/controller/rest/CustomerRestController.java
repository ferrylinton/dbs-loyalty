package com.dbs.loyalty.web.controller.rest;

import static com.dbs.loyalty.config.constant.MessageConstant.DATA_IS_NOT_FOUND;
import static com.dbs.loyalty.config.constant.RestConstant.CHANGE_PASSWORD;
import static com.dbs.loyalty.config.constant.RestConstant.GET_CUSTOMER_INFO;
import static com.dbs.loyalty.config.constant.RestConstant.UPDATE_CUSTOMER;
import static com.dbs.loyalty.config.constant.RestConstant.UPDATE_CUSTOMER_NOTES;
import static com.dbs.loyalty.config.constant.SwaggerConstant.CUSTOMER;
import static com.dbs.loyalty.config.constant.SwaggerConstant.JSON;
import static com.dbs.loyalty.config.constant.SwaggerConstant.JWT;
import static com.dbs.loyalty.config.constant.SwaggerConstant.OK;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.dbs.loyalty.config.constant.MessageConstant;
import com.dbs.loyalty.config.constant.SecurityConstant;
import com.dbs.loyalty.domain.cst.Customer;
import com.dbs.loyalty.exception.NotFoundException;
import com.dbs.loyalty.model.Pair;
import com.dbs.loyalty.model.TokenData;
import com.dbs.loyalty.security.rest.RestTokenProvider;
import com.dbs.loyalty.service.CustomerService;
import com.dbs.loyalty.service.dto.CustomerDto;
import com.dbs.loyalty.service.dto.CustomerPasswordDto;
import com.dbs.loyalty.service.dto.CustomerUpdateDto;
import com.dbs.loyalty.service.dto.JWTTokenDto;
import com.dbs.loyalty.service.mapper.CustomerMapper;
import com.dbs.loyalty.util.HeaderTokenUtil;
import com.dbs.loyalty.util.SecurityUtil;
import com.dbs.loyalty.web.response.Response;
import com.dbs.loyalty.web.validator.CustomerPasswordValidator;
import com.dbs.loyalty.web.validator.CustomerUpdateValidator;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.annotations.Authorization;
import lombok.RequiredArgsConstructor;

@Api(tags = { CUSTOMER })
@RequiredArgsConstructor
@PreAuthorize("hasRole('CUSTOMER')")
@RestController
public class CustomerRestController {

    private final CustomerService customerService;

    private final RestTokenProvider restTokenProvider;
    
    private final CustomerMapper customerMapper;
    
    @ApiOperation(value=GET_CUSTOMER_INFO, produces=JSON, authorizations={@Authorization(value=JWT)})
    @ApiResponses(value={@ApiResponse(code=200, message=OK, response = CustomerDto.class)})
    @GetMapping("/api/customers/info")
	public CustomerDto getCustomerInfo() throws NotFoundException{
    	Optional<Customer> current = customerService.findByEmail(SecurityUtil.getLogged());
		
		if(current.isPresent()) {
			return customerMapper.toDto(current.get());
		}else {
			throw new NotFoundException(String.format(DATA_IS_NOT_FOUND, CUSTOMER, SecurityUtil.getLogged()));
		}
	}
    
    @ApiOperation(value=UPDATE_CUSTOMER, notes=UPDATE_CUSTOMER_NOTES, consumes=JSON, produces=JSON, authorizations={@Authorization(value=JWT)})
    @ApiResponses(value={@ApiResponse(code=200, message=OK, response = JWTTokenDto.class)})
    @PutMapping("/api/customers")
    public JWTTokenDto updateCustomer(
    		@ApiParam(name = "CustomerNewData", value = "Customer's new data") 
    		@Valid @RequestBody CustomerUpdateDto customerUpdateDto,
    		HttpServletRequest request) throws IOException  {
    	
    	String token = null;
    	CustomerDto customerDto = customerMapper.toDto(customerService.update(customerUpdateDto));
		String jwt = HeaderTokenUtil.resolveToken(request);
        
        if (StringUtils.hasText(jwt) && restTokenProvider.validateToken(jwt)) {
        	TokenData tokenData = new TokenData();
        	tokenData.setId(customerDto.getId());
        	tokenData.setEmail(customerDto.getEmail());
        	tokenData.setRole(SecurityConstant.ROLE_CUSTOMER);
            token = restTokenProvider.createToken(tokenData, restTokenProvider.getExpiration(jwt));
        }
        
		return new JWTTokenDto(token, customerDto);
    }
    
    @ApiOperation(value=CHANGE_PASSWORD, consumes=JSON, produces=JSON, authorizations={@Authorization(value=JWT)})
    @ApiResponses(value={@ApiResponse(code=200, message=OK, response = Pair.class)})
    @PutMapping("/api/customers/change-password")
    public Response changePassword(
    		@ApiParam(name = "ChangePasswordData", value = "Customer's password data") 
    		@Valid @RequestBody CustomerPasswordDto customerPasswordDto)  {
    	
    	customerService.changePassword(customerPasswordDto);
		return new Response(MessageConstant.DATA_IS_SAVED);
    }
    
    @InitBinder("customerUpdateDto")
	protected void initBinder(WebDataBinder binder) {
		binder.registerCustomEditor(Date.class, new CustomDateEditor(new SimpleDateFormat("dd-MM-yyyy"), true, 10)); 
		binder.addValidators(new CustomerUpdateValidator(customerService));
	}
    
    @InitBinder("customerPasswordDto")
	protected void initPasswordBinder(WebDataBinder binder) {
		binder.addValidators(new CustomerPasswordValidator(customerService));
	}
    
}
