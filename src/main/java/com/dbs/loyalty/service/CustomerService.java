package com.dbs.loyalty.service;

import java.io.IOException;
import java.time.Instant;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.dbs.loyalty.domain.Customer;
import com.dbs.loyalty.domain.FileImage;
import com.dbs.loyalty.domain.Task;
import com.dbs.loyalty.domain.enumeration.TaskOperation;
import com.dbs.loyalty.repository.CustomerRepository;
import com.dbs.loyalty.service.dto.CustomerPasswordDto;
import com.dbs.loyalty.service.dto.CustomerUpdateDto;
import com.dbs.loyalty.service.specification.CustomerSpecification;
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
	
	public Page<Customer> findAll(Pageable pageable, HttpServletRequest request) {
		return customerRepository.findAll(CustomerSpecification.getSpec(request), pageable);
	}
	
	public boolean isEmailExist(String id, String email) {
		Optional<Customer> customer = customerRepository.findByEmailIgnoreCase(email);

		if (customer.isPresent()) {
			return (id == null) || (!id.equals(customer.get().getId()));
		}else {
			return false;
		}
	}
	
	public FileImage updateCustomerImage(MultipartFile file) throws IOException {
		return imageService.updateCustomerImage(file);
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
	
	public void changePassword(CustomerPasswordDto customerPasswordDto) {
		String passwordHash = PasswordUtil.encode(customerPasswordDto.getNewPassword());
		customerRepository.changePassword(passwordHash, SecurityUtil.getLogged());
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
				customerRepository.save(customer);
				imageService.update(customer.getId(), customer.getImage(), task.getMaker(), task.getMadeDate());
			}else if(task.getTaskOperation() == TaskOperation.DELETE) {
				customerRepository.delete(customer);
			}
		}

		return customer.getEmail();
	}

}
