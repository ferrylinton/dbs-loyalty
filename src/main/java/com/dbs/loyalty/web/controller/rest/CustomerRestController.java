package com.dbs.loyalty.web.controller.rest;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dbs.loyalty.config.constant.Constant;
import com.dbs.loyalty.config.constant.SwaggerConstant;
import com.dbs.loyalty.model.ErrorResponse;
import com.dbs.loyalty.model.Pair;
import com.dbs.loyalty.service.CustomerService;
import com.dbs.loyalty.service.dto.CustomerDto;
import com.dbs.loyalty.service.dto.CustomerPasswordDto;
import com.dbs.loyalty.service.dto.CustomerUpdateDto;
import com.dbs.loyalty.util.Base64Util;
import com.dbs.loyalty.util.SecurityUtil;
import com.dbs.loyalty.web.controller.AbstractController;
import com.dbs.loyalty.web.validator.CustomerUpdateValidator;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.annotations.Authorization;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Api(tags = { SwaggerConstant.Customer })
@RequiredArgsConstructor
@Slf4j
@RestController
@RequestMapping("/api")
public class CustomerRestController extends AbstractController{

    private final CustomerService customerService;
    
    @ApiOperation(
    		nickname="GetCustomerInfo", 
    		value="GetCustomerInfo", 
    		notes="Get Customer Information",
    		produces=MediaType.APPLICATION_JSON_VALUE, 
    		authorizations = { @Authorization(value=SwaggerConstant.JWT) })
    @ApiResponses(value={@ApiResponse(code=200, message="OK", response = CustomerDto.class)})
    @PreAuthorize("hasRole('CUSTOMER')")
    @GetMapping("/customers/info")
	public ResponseEntity<?> getCustomerInfo(){
    	try {
			Optional<CustomerDto> customerDto = customerService.findByEmail(SecurityUtil.getCurrentEmail());
			return ResponseEntity.ok().body(customerDto.get());
    	} catch (Exception e) {
			log.error(e.getLocalizedMessage(), e);
			return ResponseEntity.status(500).body(new ErrorResponse(e.getLocalizedMessage()));
		}
	}
    
    @ApiOperation(
    		nickname="GetCustomerImage", 
    		value="GetCustomerImage", 
    		notes="Get Customer Image", 
    		produces=MediaType.IMAGE_JPEG_VALUE, 
    		authorizations = { @Authorization(value="JWT") })
    @ApiResponses(value={@ApiResponse(code=200, message="OK", response = Byte.class)})
    @PreAuthorize("hasRole('CUSTOMER')")
    @GetMapping(value = "/customers/image", produces = MediaType.IMAGE_JPEG_VALUE)
	public ResponseEntity<?> getCustomerImage() {
    	try {
	    	Optional<CustomerDto> customerDto = customerService.findByEmail(SecurityUtil.getCurrentEmail());
			return ResponseEntity.ok().body(Base64Util.getBytes(customerDto.get().getImageString()));
    	} catch (Exception e) {
			log.error(e.getLocalizedMessage(), e);
			return ResponseEntity.status(500).body(new ErrorResponse(e.getLocalizedMessage()));
		}
	}
    
    @ApiOperation(
    		nickname="UpdateCustomer", 
    		value="UpdateCustomer", 
    		notes="Update Customer Information",
    		consumes=MediaType.APPLICATION_JSON_VALUE,
    		produces=MediaType.APPLICATION_JSON_VALUE, 
    		authorizations = { @Authorization(value=SwaggerConstant.JWT) })
    @PreAuthorize("hasRole('CUSTOMER')")
    @PutMapping("/customers")
    @ApiResponses(value={@ApiResponse(code=200, message="OK", response = CustomerDto.class)})
    public ResponseEntity<?> updateCustomer(
    		@ApiParam(name = "CustomerData", value = "Customer's new data") 
    		@Valid @RequestBody CustomerUpdateDto customerUpdateDto)  {
    	try {
    		return ResponseEntity.ok().body(customerService.update(customerUpdateDto));
    	} catch (Exception e) {
			log.error(e.getLocalizedMessage(), e);
			return ResponseEntity.status(500).body(new ErrorResponse(e.getLocalizedMessage()));
		}
    }
    
    @ApiOperation(
    		nickname="ChangePassword", 
    		value="ChangePassword", 
    		notes="Change Customer's Password",
    		consumes=MediaType.APPLICATION_JSON_VALUE,
    		produces=MediaType.APPLICATION_JSON_VALUE, 
    		authorizations = { @Authorization(value=SwaggerConstant.JWT) })
    @PreAuthorize("hasRole('CUSTOMER')")
    @PostMapping("/customers/change-password")
    @ApiResponses(value={@ApiResponse(code=200, message="OK", response = Pair.class)})
    public ResponseEntity<?> changePassword(
    		@ApiParam(name = "ChangePasswordData", value = "Customer's new password") 
    		@Valid @RequestBody CustomerPasswordDto customerPasswordDto)  {
    	try {
    		customerService.changePassword(customerPasswordDto);
    		Pair<String, String> result = new Pair<String, String>(Constant.MESSAGE, Constant.SUCCESS);
    		return ResponseEntity.ok().body(result);
    	} catch (Exception e) {
			log.error(e.getLocalizedMessage(), e);
			return ResponseEntity.status(500).body(new ErrorResponse(e.getLocalizedMessage()));
		}
    }
    
    @InitBinder("customerUpdateDto")
	protected void initBinder(WebDataBinder binder) {
		binder.registerCustomEditor(Date.class, new CustomDateEditor(new SimpleDateFormat("dd-MM-yyyy"), true, 10)); 
		binder.addValidators(new CustomerUpdateValidator(customerService));
	}
    
}
