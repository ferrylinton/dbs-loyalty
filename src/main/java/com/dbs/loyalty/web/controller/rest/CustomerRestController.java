package com.dbs.loyalty.web.controller.rest;

import java.util.Optional;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dbs.loyalty.config.constant.SwaggerConstant;
import com.dbs.loyalty.model.ErrorResponse;
import com.dbs.loyalty.service.CustomerService;
import com.dbs.loyalty.service.dto.CustomerDto;
import com.dbs.loyalty.util.SecurityUtil;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
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
public class CustomerRestController {

    private final CustomerService customerService;

    @ApiOperation(nickname="getCustomerInfo", value="getCustomerInfo", notes="Get Customer Information",
    		produces=MediaType.APPLICATION_JSON_VALUE, authorizations = { @Authorization(value=SwaggerConstant.JWT) })
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
    
    @ApiOperation(nickname="getCustomerImage", value="getCustomerImage", notes="Get Customer Image", 
    		produces=MediaType.IMAGE_JPEG_VALUE, authorizations = { @Authorization(value="JWT") })
    @ApiResponses(value={@ApiResponse(code=200, message="OK", response = Byte.class)})
    @PreAuthorize("hasRole('CUSTOMER')")
    @GetMapping(value = "/customers/image", produces = MediaType.IMAGE_JPEG_VALUE)
	public ResponseEntity<?> getCustomerImage() {
    	try {
	    	Optional<CustomerDto> customerDto = customerService.findByEmail(SecurityUtil.getCurrentEmail());
			return ResponseEntity.ok().body(customerDto.get().getImageBytes());
    	} catch (Exception e) {
			log.error(e.getLocalizedMessage(), e);
			return ResponseEntity.status(500).body(new ErrorResponse(e.getLocalizedMessage()));
		}
	}
    
}
