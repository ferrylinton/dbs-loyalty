package com.dbs.loyalty.service;

import java.io.IOException;
import java.time.Instant;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.dbs.loyalty.domain.Customer;
import com.dbs.loyalty.domain.enumeration.TaskOperation;
import com.dbs.loyalty.repository.CustomerRepository;
import com.dbs.loyalty.service.dto.CustomerDto;
import com.dbs.loyalty.service.dto.CustomerPasswordDto;
import com.dbs.loyalty.service.dto.CustomerUpdateDto;
import com.dbs.loyalty.service.dto.TaskDto;
import com.dbs.loyalty.service.mapper.CustomerMapper;
import com.dbs.loyalty.service.specification.CustomerSpecification;
import com.dbs.loyalty.util.Base64Util;
import com.dbs.loyalty.util.PasswordUtil;
import com.dbs.loyalty.util.SecurityUtil;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class CustomerService{

	private final ObjectMapper objectMapper;
	
	private final CustomerRepository customerRepository;
	
	private final CustomerMapper customerMapper;
	
	private final UrlService urlService;

	public Optional<CustomerDto> findByEmail(String email){
		return customerRepository.findByEmail(email).map(customer -> customerMapper.toDto(customer, urlService));
	}
	
	public Optional<CustomerDto> findById(String id) {
		return customerRepository.findById(id).map(customer -> customerMapper.toDto(customer, urlService));
	}
	
	public Page<CustomerDto> findAll(Pageable pageable, HttpServletRequest request) {
		return customerRepository.findAll(CustomerSpecification.getSpec(request), pageable).map(customerMapper::toDto);
	}
	
	public boolean isEmailExist(CustomerDto customerDto) {
		Optional<Customer> customer = customerRepository.findByEmailIgnoreCase(customerDto.getEmail());

		if (customer.isPresent()) {
			return (customerDto.getId() == null) || (!customerDto.getId().equals(customer.get().getId()));
		}else {
			return false;
		}
	}
	
	public boolean isEmailExist(CustomerUpdateDto customerUpdateDto) {
		Optional<Customer> customer = customerRepository.findByEmailIgnoreCase(customerUpdateDto.getEmail());

		if (customer.isPresent()) {
			return (customerUpdateDto.getId() == null) || (!customerUpdateDto.getId().equals(customer.get().getId()));
		}else {
			return false;
		}
	}
	
	public CustomerDto update(CustomerUpdateDto customerUpdateDto) {
		Optional<Customer> current = customerRepository.findByEmail(SecurityUtil.getLogged());
		
		if(current.isPresent()) {
			Customer customer = current.get();
			customer.setEmail(customerUpdateDto.getEmail());
			customer.setName(customerUpdateDto.getName());
			customer.setPhone(customerUpdateDto.getPhone());
			customer.setDob(customerUpdateDto.getDob());
			customer.setImageBytes(Base64Util.getBytes(customerUpdateDto.getImageString()));
			customer.setLastModifiedBy(SecurityUtil.getLogged());
			customer.setLastModifiedDate(Instant.now());
			
			customer = customerRepository.save(customer);
			return customerMapper.toDto(customer, urlService);
		}else {
			return null;
		}
	}
	
	public void changePassword(CustomerPasswordDto customerPasswordDto) {
		String passwordHash = PasswordUtil.getInstance().encode(customerPasswordDto.getNewPassword());
		customerRepository.changePassword(passwordHash, SecurityUtil.getLogged());
	}
	
	public String execute(TaskDto taskDto) throws IOException {
		CustomerDto dto = objectMapper.readValue((taskDto.getTaskOperation() == TaskOperation.DELETE) ? taskDto.getTaskDataOld() : taskDto.getTaskDataNew(), CustomerDto.class);
		
		if(taskDto.isVerified()) {
			Customer customer = customerMapper.toEntity(dto);
			if(taskDto.getTaskOperation() == TaskOperation.ADD) {
				customer.setCreatedBy(taskDto.getMaker());
				customer.setCreatedDate(taskDto.getMadeDate());
				customerRepository.save(customer);
			}else if(taskDto.getTaskOperation() == TaskOperation.MODIFY) {
				customer.setLastModifiedBy(taskDto.getMaker());
				customer.setLastModifiedDate(taskDto.getMadeDate());
				customerRepository.save(customer);
			}else if(taskDto.getTaskOperation() == TaskOperation.DELETE) {
				customerRepository.delete(customer);
			}
		}

		return dto.getEmail();
	}

}
