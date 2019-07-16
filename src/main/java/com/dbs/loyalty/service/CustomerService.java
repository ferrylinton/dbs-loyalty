package com.dbs.loyalty.service;

import java.io.IOException;
import java.time.Instant;
import java.util.Map;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.dbs.loyalty.domain.Task;
import com.dbs.loyalty.domain.cst.Customer;
import com.dbs.loyalty.enumeration.TaskOperation;
import com.dbs.loyalty.repository.CustomerRepository;
import com.dbs.loyalty.service.dto.CustomerNewPasswordDto;
import com.dbs.loyalty.service.dto.CustomerPasswordDto;
import com.dbs.loyalty.service.dto.CustomerUpdateDto;
import com.dbs.loyalty.service.specification.CustomerSpec;
import com.dbs.loyalty.util.PasswordUtil;
import com.dbs.loyalty.util.SecurityUtil;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class CustomerService{

	private final CustomerRepository customerRepository;
	
	private final ImageService imageService;
	
	private final ObjectMapper objectMapper;

	public Optional<Customer> findById(String id) {
		return customerRepository.findById(id);
	}
	
	public Optional<Customer> findByEmail(String email){
		return customerRepository.findByEmail(email);
	}
	
	public Page<Customer> findAll(Map<String, String> params, Pageable pageable) {
		return customerRepository.findAll(new CustomerSpec(params), pageable);
	}
	
	public boolean isEmailExist(String id, String email) {
		Optional<Customer> customer = customerRepository.findByEmailIgnoreCase(email);

		if (customer.isPresent()) {
			return (id == null) || (!id.equals(customer.get().getId()));
		}else {
			return false;
		}
	}

	public void save(boolean pending, String id) {
		customerRepository.save(pending, id);
	}
	
	public Customer save(Customer customer) {
		return customerRepository.save(customer);
	}
	
	public Customer update(CustomerUpdateDto customerUpdateDto) {
		Optional<Customer> current = customerRepository.findByEmail(SecurityUtil.getLogged());
		
		if(current.isPresent()) {
			Customer customer = current.get();
			customer.setEmail(customerUpdateDto.getEmail());
			customer.setName(customerUpdateDto.getName());
			customer.setPhone(customerUpdateDto.getPhone());
			customer.setLastModifiedBy(SecurityUtil.getLogged());
			customer.setLastModifiedDate(Instant.now());
			
			return customerRepository.save(customer);
		}else {
			return null;
		}
	}
	
	public void changePassword(CustomerPasswordDto customerPassword) {
		String passwordHash = PasswordUtil.encode(customerPassword.getNewPassword());
		customerRepository.changePassword(passwordHash, SecurityUtil.getLogged());
	}
	
	public void activate(CustomerNewPasswordDto customerActivateDto) {
		Optional<Customer> customer = customerRepository.findByEmailIgnoreCase(SecurityUtil.getLogged());
		
		if(customer.isPresent()) {
			customer.get().setActivated(true);
			customer.get().setLocked(false);
			customer.get().setPasswordHash(PasswordUtil.encode(customerActivateDto.getPassword()));
			customer.get().setLastModifiedBy(SecurityUtil.getLogged());
			customer.get().setLastModifiedDate(Instant.now());
			customerRepository.save(customer.get());
		}
	}
	
	public String execute(Task task) throws IOException {
		Customer customer = objectMapper.readValue((task.getTaskOperation() == TaskOperation.DELETE) ? task.getTaskDataOld() : task.getTaskDataNew(), Customer.class);

		if(task.getVerified()) {
			if(task.getTaskOperation() == TaskOperation.ADD) {
				customer.setCreatedBy(task.getMaker());
				customer.setCreatedDate(task.getMadeDate());
				customerRepository.save(customer);
				imageService.add(customer.getId(), customer.getImage(), task.getMaker(), task.getMadeDate());
			}else if(task.getTaskOperation() == TaskOperation.MODIFY) {
				customer.setLastModifiedBy(task.getMaker());
				customer.setLastModifiedDate(task.getMadeDate());
				customer.setPending(false);
				customerRepository.save(customer);
				imageService.update(customer.getId(), customer.getImage(), task.getMaker(), task.getMadeDate());
			}else if(task.getTaskOperation() == TaskOperation.DELETE) {
				customerRepository.delete(customer);
			}
		}else if(task.getTaskOperation() != TaskOperation.ADD) {
			customerRepository.save(false, customer.getId());
		}

		return customer.getEmail();
	}

}
