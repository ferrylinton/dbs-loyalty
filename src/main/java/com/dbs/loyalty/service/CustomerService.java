package com.dbs.loyalty.service;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.ui.ModelMap;

import com.dbs.loyalty.config.Constant;
import com.dbs.loyalty.domain.Customer;
import com.dbs.loyalty.exception.NotFoundException;
import com.dbs.loyalty.model.ErrorResponse;
import com.dbs.loyalty.repository.CustomerRepository;
import com.dbs.loyalty.util.PasswordUtil;
import com.dbs.loyalty.util.ResponseUtil;
import com.dbs.loyalty.util.UrlUtil;


@Service
public class CustomerService{
	
	private final Logger LOG = LoggerFactory.getLogger(CustomerService.class);

	private final String ENTITY_NAME = "customer";

	private final CustomerRepository customerRepository;
 
	public CustomerService(CustomerRepository customerRepository) {
		this.customerRepository = customerRepository;
	}
	
	public Optional<Customer> findByEmail(String email){
		return customerRepository.findByEmail(email);
	}
	
	public Optional<Customer> findById(String id) {
		return customerRepository.findById(id);
	}
	
	public Page<Customer> findAll(String keyword, Pageable pageable) {
		if(keyword.equals(Constant.EMPTY)) {
			return customerRepository.findAll(pageable);
		}else {
			return customerRepository.findAllByEmailContainingAllIgnoreCase(keyword, pageable);
		}
	}
	
	public boolean isEmailExist(Customer customer) {
		return isExist(customerRepository.findByEmailIgnoreCase(customer.getEmail()), customer.getId());
	}
	
	private boolean isExist(Optional<Customer> customer, String id) {
		if(customer.isPresent()) {
			if(id == null) {
				return true;
			}else if(!id.equals(customer.get().getId())) {
				return true;
			}
		}
		
		return false;
	}
	
	public void viewForm(ModelMap model, String id) throws NotFoundException {
		if(id.equals(Constant.ZERO)) {
			model.addAttribute(ENTITY_NAME, new Customer());
		}else {
			Optional<Customer> customer = findById(id);
			model.addAttribute(ENTITY_NAME, customer.get());
		}
	}
	
	public ResponseEntity<?> save(Customer customer) {
		try {
			if(customer.getId() == null) {
				customer.setPasswordHash(PasswordUtil.getInstance().encode(customer.getPasswordPlain()));
			}else {
				Optional<Customer> current = customerRepository.findById(customer.getId());
				customer.setPasswordHash(current.get().getPasswordHash());
				customer.setImageBytes(current.get().getImageBytes());
			}
			
			customerRepository.save(customer);
			return ResponseUtil.createSaveResponse(customer.getEmail(), ENTITY_NAME);
		} catch (Exception ex) {
			LOG.error(ex.getLocalizedMessage(), ex);
			return ResponseEntity
		            .status(HttpStatus.INTERNAL_SERVER_ERROR)
		            .body(new ErrorResponse(ex.getLocalizedMessage()));
		}
	}
	
	public ResponseEntity<?> delete(String id) throws NotFoundException {
		try {
			Optional<Customer> customer = customerRepository.findById(id);
			customerRepository.delete(customer.get());
			return ResponseUtil.createDeleteResponse(customer.get().getEmail(), ENTITY_NAME);
		} catch (Exception ex) {
			LOG.error(ex.getLocalizedMessage(), ex);
			return ResponseEntity
		            .status(HttpStatus.INTERNAL_SERVER_ERROR)
		            .body(new ErrorResponse(ex.getLocalizedMessage()));
		}
	}

}
