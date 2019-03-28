package com.dbs.loyalty.service;

import java.io.IOException;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.dbs.loyalty.domain.Customer;
import com.dbs.loyalty.domain.enumeration.TaskOperation;
import com.dbs.loyalty.repository.CustomerRepository;
import com.dbs.loyalty.service.dto.CustomerDto;
import com.dbs.loyalty.service.dto.TaskDto;
import com.dbs.loyalty.service.mapper.CustomerMapper;
import com.dbs.loyalty.service.specification.CustomerSpecification;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class CustomerService{

	private final ObjectMapper objectMapper;
	
	private final CustomerRepository customerRepository;
	
	private final CustomerMapper customerMapper;

	public Optional<CustomerDto> findByEmail(String email){
		return customerRepository.findByEmail(email).map(customerMapper::toDto);
	}
	
	public Optional<CustomerDto> findById(String id) {
		return customerRepository.findById(id).map(customerMapper::toDto);
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
	
	public String execute(TaskDto taskDto) throws JsonParseException, JsonMappingException, IOException {
		CustomerDto dto = objectMapper.readValue((taskDto.getTaskOperation() == TaskOperation.Delete) ? taskDto.getTaskDataOld() : taskDto.getTaskDataNew(), CustomerDto.class);
		
		if(taskDto.isVerified()) {
			Customer customer = customerMapper.toEntity(dto);
			if(taskDto.getTaskOperation() == TaskOperation.Add) {
				customer.setCreatedBy(taskDto.getMaker());
				customer.setCreatedDate(taskDto.getMadeDate());
				customerRepository.save(customer);
			}else if(taskDto.getTaskOperation() == TaskOperation.Modify) {
				customer.setLastModifiedBy(taskDto.getMaker());
				customer.setLastModifiedDate(taskDto.getMadeDate());
				customerRepository.save(customer);
			}else if(taskDto.getTaskOperation() == TaskOperation.Delete) {
				customerRepository.delete(customer);
			}
		}

		return dto.getEmail();
	}

}
