package com.dbs.loyalty.web.controller.rest;


import java.beans.PropertyEditorSupport;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dbs.loyalty.aop.EnableLogAuditCustomer;
import com.dbs.loyalty.config.constant.DateConstant;
import com.dbs.loyalty.config.constant.MessageConstant;
import com.dbs.loyalty.config.constant.SecurityConstant;
import com.dbs.loyalty.config.constant.SwaggerConstant;
import com.dbs.loyalty.domain.Customer;
import com.dbs.loyalty.exception.BadRequestException;
import com.dbs.loyalty.exception.NotFoundException;
import com.dbs.loyalty.model.TokenData;
import com.dbs.loyalty.security.rest.RestTokenProvider;
import com.dbs.loyalty.service.CustomerService;
import com.dbs.loyalty.service.LogAuditCustomerService;
import com.dbs.loyalty.service.dto.CustomerDto;
import com.dbs.loyalty.service.dto.CustomerNewPasswordDto;
import com.dbs.loyalty.service.dto.CustomerPasswordDto;
import com.dbs.loyalty.service.dto.CustomerUpdateDto;
import com.dbs.loyalty.service.dto.JWTTokenDto;
import com.dbs.loyalty.service.mapper.CustomerMapper;
import com.dbs.loyalty.util.HeaderTokenUtil;
import com.dbs.loyalty.util.SecurityUtil;
import com.dbs.loyalty.util.UrlUtil;
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

@Api(tags = { SwaggerConstant.CUSTOMER })
@RequiredArgsConstructor
@PreAuthorize("hasRole('CUSTOMER')")
@RestController
@RequestMapping("/api/customers")
public class CustomerRestController {
	
	public static final String GET_CUSTOMER_INFO = "GetCustomerInfo";
	
	public static final String UPDATE_CUSTOMER = "UpdateCustomer";
	
	public static final String UPDATE_CUSTOMER_NOTES = "Update customer information, and after update customer must use new token or relogin";
	
	public static final String CHANGE_PASSWORD = "ChangePassword";
	
	public static final String FORGOT_PASSWORD =  "ForgotPassword";

    private final CustomerService customerService;

    private final RestTokenProvider restTokenProvider;
    
    private final CustomerMapper customerMapper;
    
    private final LogAuditCustomerService logAuditCustomerService;
    
    @ApiOperation(
    		value=GET_CUSTOMER_INFO, 
    		produces="application/json", 
    		authorizations={@Authorization(value="JWT")})
    @ApiResponses(value={@ApiResponse(code=200, message="OK", response = CustomerDto.class)})
    @EnableLogAuditCustomer(operation=GET_CUSTOMER_INFO)
    @GetMapping("/info")
	public CustomerDto getCustomerInfo(HttpServletRequest request, HttpServletResponse response) throws NotFoundException{
    	Optional<Customer> current = customerService.findByEmail(SecurityUtil.getLogged());
		
		if(current.isPresent()) {
			return customerMapper.toDto(current.get());
		}else {
			throw new NotFoundException(String.format(MessageConstant.DATA_IS_NOT_FOUND, SwaggerConstant.CUSTOMER, SecurityUtil.getLogged()));
		}
	}
    
    @ApiOperation(
    		value=UPDATE_CUSTOMER, 
    		notes=UPDATE_CUSTOMER_NOTES, 
    		consumes="application/json", 
    		produces="application/json", 
    		authorizations={@Authorization(value="JWT")})
    @ApiResponses(value={@ApiResponse(code=200, message="OK", response = JWTTokenDto.class)})
    @PutMapping
    public JWTTokenDto updateCustomer(
    		@ApiParam(name = "CustomerNewData", value = "Customer's new data") 
    		@Valid @RequestBody CustomerUpdateDto requestDataCustomer,
    		HttpServletRequest request) throws IOException  {

    	Optional<Customer> current = customerService.findByEmail(SecurityUtil.getLogged());
    	CustomerDto customerDto = customerMapper.toDto(customerService.update(requestDataCustomer));
    	logAuditCustomerService.save(UPDATE_CUSTOMER, UrlUtil.getFullUrl(request), requestDataCustomer, customerMapper.toDto(current.get()));
		return createToken(customerDto, request);
    }
    
    @ApiOperation(
    		value=CHANGE_PASSWORD, 
    		consumes="application/json", 
    		produces="application/json", 
    		authorizations={@Authorization(value="JWT")})
    @ApiResponses(value={@ApiResponse(code=200, message="OK", response = Response.class)})
    @EnableLogAuditCustomer(operation=CHANGE_PASSWORD)
    @PostMapping("/change-password")
    public Response changePassword(
    		@ApiParam(name = "ChangePasswordData", value = "Customer's password data") 
    		@Valid @RequestBody CustomerPasswordDto requestDataPassword,
    		HttpServletRequest request)  {
    	
    	customerService.changePassword(requestDataPassword);
    	logAuditCustomerService.save(UPDATE_CUSTOMER, UrlUtil.getFullUrl(request), requestDataPassword);
		return new Response(MessageConstant.DATA_IS_SAVED);
    }

    @ApiOperation(
    		value=FORGOT_PASSWORD, 
    		produces="application/json", 
    		authorizations={@Authorization(value="JWT")})
    @ApiResponses(value={@ApiResponse(code=200, message="OK", response=Response.class)})
    @PreAuthorize("hasRole('TOKEN')")
    @EnableLogAuditCustomer(operation=FORGOT_PASSWORD)
    @PutMapping("/forgot")
    public Response forgot(
    		@ApiParam(name = "CustomerNewPassword", value = "Customer's new password") 
    		@Valid @RequestBody CustomerNewPasswordDto requestData,
    		HttpServletRequest request) throws BadRequestException  {
    	
    	if(!requestData.getPassword().equals(requestData.getConfirmPassword())) {
    		throw new BadRequestException(MessageConstant.PASSWORD_IS_NOT_CONFIRMED);
    	}
    	
    	customerService.changePassword(requestData);
    	logAuditCustomerService.save(UPDATE_CUSTOMER, UrlUtil.getFullUrl(request), requestData);
    	return new Response(MessageConstant.CUSTOMER_IS_ACTIVATED);
    }
    
    private JWTTokenDto createToken(CustomerDto customerDto, HttpServletRequest request) throws IOException {
    	String token = null;
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
    
    @InitBinder("requestDataCustomer")
	protected void initBinder(WebDataBinder binder) {
		binder.registerCustomEditor(LocalDate.class, new PropertyEditorSupport() {
			
		    @Override
		    public void setAsText(String text) throws IllegalArgumentException{
		      setValue(LocalDate.parse(text, DateTimeFormatter.ofPattern(DateConstant.JAVA_DATE)));
		    }

		    @Override
		    public String getAsText() throws IllegalArgumentException {
		      return DateTimeFormatter.ofPattern(DateConstant.JAVA_DATE).format((LocalDate) getValue());
		    }
		    
		});
		
		binder.addValidators(new CustomerUpdateValidator(customerService));
	}
    
    @InitBinder("requestDataPassword")
	protected void initPasswordBinder(WebDataBinder binder) {
		binder.addValidators(new CustomerPasswordValidator(customerService));
	}
    
}
