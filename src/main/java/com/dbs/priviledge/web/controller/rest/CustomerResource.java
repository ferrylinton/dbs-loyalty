package com.dbs.priviledge.web.controller.rest;
import java.util.Optional;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dbs.priviledge.domain.Customer;
import com.dbs.priviledge.exception.NotFoundException;
import com.dbs.priviledge.service.CustomerService;

/**
 * REST controller for managing Customer.
 */
@RestController
@RequestMapping("/api/customers")
public class CustomerResource {

    private final CustomerService customerService;

    public CustomerResource(CustomerService customerService) {
        this.customerService = customerService;
    }

    @GetMapping("/{id}")
	public Customer getCustomer(@PathVariable String id) throws NotFoundException {
		Optional<Customer> customer = customerService.findById(id);
		return customer.get();
	}
    
    @GetMapping(value = "/{id}/image", produces = MediaType.IMAGE_JPEG_VALUE)
	public byte[] getImage(@PathVariable String id) throws NotFoundException {
		Optional<Customer> customer = customerService.findById(id);
		return customer.get().getImageBytes();
	}
    
}
