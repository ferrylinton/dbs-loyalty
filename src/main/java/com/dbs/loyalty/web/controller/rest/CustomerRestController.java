package com.dbs.loyalty.web.controller.rest;

import java.util.Optional;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dbs.loyalty.config.constant.SwaggerConstant;
import com.dbs.loyalty.service.CustomerService;
import com.dbs.loyalty.service.dto.CustomerDto;
import com.dbs.loyalty.util.SecurityUtil;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.Authorization;
import lombok.RequiredArgsConstructor;

@Api(tags = { SwaggerConstant.Customer })
@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
public class CustomerRestController {

    private final CustomerService customerService;

    @ApiOperation(value = "Get Customer Information", authorizations = { @Authorization(value="JWT") })
    @GetMapping("/customers/info")
	public ResponseEntity<?> getCustomerById(){
		Optional<CustomerDto> customerDto = customerService.findByEmail(SecurityUtil.getCurrentEmail());
		return ResponseEntity.ok().body(customerDto.get());
	}
    
    @ApiOperation(value = "Get Customer Image", authorizations = { @Authorization(value="JWT") })
    @GetMapping(value = "/customers/image", produces = MediaType.IMAGE_JPEG_VALUE)
	public byte[] getImage() {
    	Optional<CustomerDto> customerDto = customerService.findByEmail(SecurityUtil.getCurrentEmail());
		return customerDto.get().getImageBytes();
	}
    
}
