package com.dbs.loyalty.service;

import java.io.IOException;
import java.time.Instant;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.dbs.loyalty.domain.Customer;
import com.dbs.loyalty.domain.FileImage;
import com.dbs.loyalty.domain.enumeration.TaskOperation;
import com.dbs.loyalty.repository.CustomerRepository;
import com.dbs.loyalty.repository.FileImageRepository;
import com.dbs.loyalty.service.dto.CustomerDto;
import com.dbs.loyalty.service.dto.CustomerFormDto;
import com.dbs.loyalty.service.dto.CustomerPasswordDto;
import com.dbs.loyalty.service.dto.CustomerUpdateDto;
import com.dbs.loyalty.service.dto.CustomerViewDto;
import com.dbs.loyalty.service.dto.TaskDto;
import com.dbs.loyalty.service.mapper.CustomerMapper;
import com.dbs.loyalty.service.specification.CustomerSpecification;
import com.dbs.loyalty.util.ImageUtil;
import com.dbs.loyalty.util.PasswordUtil;
import com.dbs.loyalty.util.SecurityUtil;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class CustomerService{

	private final ObjectMapper objectMapper;
	
	private final CustomerRepository customerRepository;
	
	private final FileImageRepository fileImageRepository;
	
	private final CustomerMapper customerMapper;

	public Optional<CustomerViewDto> findViewDtoByEmail(String email){
		return customerRepository
				.findByEmail(email)
				.map(customerMapper::toViewDto);
	}
	
	public Optional<Customer> findByEmail(String email){
		return customerRepository.findByEmail(email);
	}
	
	public Optional<CustomerDto> findWithCustomerImageById(String id) {
		return customerRepository
				.findById(id)
				.map(customerMapper::toDto);
	}
	
	public Page<CustomerDto> findAll(Pageable pageable, HttpServletRequest request) {
		return customerRepository
				.findAll(CustomerSpecification.getSpec(request), pageable)
				.map(customerMapper::toDto);
	}
	
	public boolean isEmailExist(String id, String email) {
		Optional<Customer> customer = customerRepository.findByEmailIgnoreCase(email);

		if (customer.isPresent()) {
			return (id == null) || (!id.equals(customer.get().getId()));
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
	
	@Transactional
	public FileImage save(MultipartFile file) throws IOException {
		Optional<Customer> current = customerRepository.findByEmail(SecurityUtil.getLogged());
		
		if(current.isPresent()) {
			Customer customer = current.get();
			customer.setLastModifiedBy(SecurityUtil.getLogged());
			customer.setLastModifiedDate(Instant.now());
			customer = customerRepository.save(customer);
			
			FileImage fileImage = new FileImage();
			fileImage.setId(customer.getId());
			fileImage.setLastModifiedBy(SecurityUtil.getLogged());
			fileImage.setLastModifiedDate(Instant.now());
			ImageUtil.setFileImage(fileImage, file);
			fileImageRepository.save(fileImage);

			return fileImage;
		}else {
			return null;
		}
	}
	
	public CustomerViewDto update(CustomerUpdateDto customerUpdateDto) {
		Optional<Customer> current = customerRepository.findByEmail(SecurityUtil.getLogged());
		
		if(current.isPresent()) {
			Customer customer = current.get();
			customer.setEmail(customerUpdateDto.getEmail());
			customer.setName(customerUpdateDto.getName());
			customer.setPhone(customerUpdateDto.getPhone());
			customer.setLastModifiedBy(SecurityUtil.getLogged());
			customer.setLastModifiedDate(Instant.now());
			
			customer = customerRepository.save(customer);
			return customerMapper.toViewDto(customer);
		}else {
			return null;
		}
	}
	
	public void changePassword(CustomerPasswordDto customerPasswordDto) {
		String passwordHash = PasswordUtil.encode(customerPasswordDto.getNewPassword());
		customerRepository.changePassword(passwordHash, SecurityUtil.getLogged());
	}
	
	public String execute(TaskDto taskDto) throws IOException {
		CustomerFormDto dto = objectMapper.readValue((taskDto.getTaskOperation() == TaskOperation.DELETE) ? taskDto.getTaskDataOld() : taskDto.getTaskDataNew(), CustomerFormDto.class);

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
