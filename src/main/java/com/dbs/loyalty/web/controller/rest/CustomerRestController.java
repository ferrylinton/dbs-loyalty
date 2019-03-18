package com.dbs.loyalty.web.controller.rest;

import java.util.Optional;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dbs.loyalty.config.TagConstant;
import com.dbs.loyalty.domain.Customer;
import com.dbs.loyalty.service.CustomerService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.Authorization;

@Api(tags = { TagConstant.Customer })
@RestController
@RequestMapping("/api")
public class CustomerRestController {

    private final CustomerService customerService;

    public CustomerRestController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @ApiOperation(value = "Get Customer by Id", authorizations = { @Authorization(value="JWT") })
    @GetMapping("/customers/{id}")
	public ResponseEntity<?> getCustomerById(@PathVariable String id){
		Optional<Customer> customer = customerService.findById(id);
		return ResponseEntity.ok().body(customer.get());
	}
    
    @ApiOperation(value = "Get Customer Image by Id", authorizations = { @Authorization(value="JWT") })
    @GetMapping(value = "/customers/{id}/image", produces = MediaType.IMAGE_JPEG_VALUE)
	public byte[] getImage(@PathVariable String id) {
		Optional<Customer> customer = customerService.findById(id);
		return customer.get().getImageBytes();
	}
    
}
