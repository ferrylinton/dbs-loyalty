package com.dbs.loyalty.service;

import java.io.IOException;
import java.time.Instant;
import java.util.Map;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.dbs.loyalty.config.constant.AddressConstant;
import com.dbs.loyalty.config.constant.DomainConstant;
import com.dbs.loyalty.domain.Address;
import com.dbs.loyalty.domain.Customer;
import com.dbs.loyalty.domain.FileImageTask;
import com.dbs.loyalty.domain.Task;
import com.dbs.loyalty.enumeration.TaskOperation;
import com.dbs.loyalty.exception.BadRequestException;
import com.dbs.loyalty.repository.AddressRepository;
import com.dbs.loyalty.repository.CustomerRepository;
import com.dbs.loyalty.service.dto.CustomerActivateDto;
import com.dbs.loyalty.service.dto.CustomerNewPasswordDto;
import com.dbs.loyalty.service.dto.CustomerPasswordDto;
import com.dbs.loyalty.service.dto.CustomerUpdateDto;
import com.dbs.loyalty.service.specification.CustomerSpec;
import com.dbs.loyalty.util.MessageUtil;
import com.dbs.loyalty.util.PasswordUtil;
import com.dbs.loyalty.util.SecurityUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class CustomerService{

	private final CustomerRepository customerRepository;
	
	private final AddressRepository addressRepository;
	
	private final ImageService imageService;
	
	private final ObjectMapper objectMapper;
	
	private final TaskService taskService;

	public Optional<Customer> findById(String id) {
		return customerRepository.findById(id);
	}
	
	public Customer getOne(String id) {
		return customerRepository.getOne(id);
	}
	
	public Optional<Customer> findByEmail(String email){
		return customerRepository.findByEmail(email);
	}

	public Customer findLoggedUserByEmail(String email) {
		Optional<Customer> customer = customerRepository.findByEmail(email);
		if(customer.isPresent()) {
			return customer.get();
		}else {
			return null;
		}
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
			customer.setFirstName(customerUpdateDto.getFirstName());
			customer.setLastName(customerUpdateDto.getLastName());
			customer.setPhone(customerUpdateDto.getPhone());
			customer.setAccountNo(customerUpdateDto.getAccountNo());
			customer.setCif(customerUpdateDto.getCif());
			customer.setDob(customerUpdateDto.getDob());
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
	
	public Customer activate(CustomerActivateDto customerActivateDto) throws BadRequestException {
		Optional<Customer> customer = customerRepository.findByEmailIgnoreCase(customerActivateDto.getEmail());
		
		if(customer.isPresent()) {
			customer.get().setActivated(true);
			customer.get().setLocked(false);
			customer.get().setPasswordHash(PasswordUtil.encode(customerActivateDto.getPassword()));
			customer.get().setLastModifiedBy(SecurityUtil.getLogged());
			customer.get().setLastModifiedDate(Instant.now());
			return customerRepository.save(customer.get());
		}else {
			throw new BadRequestException(MessageUtil.getNotFoundMessage(customerActivateDto.getEmail()));
		}
	}
	
	public void changePassword(CustomerNewPasswordDto customerNewPasswordDto) {
		Optional<Customer> customer = customerRepository.findByEmailIgnoreCase(SecurityUtil.getLogged());
		
		if(customer.isPresent()) {
			customer.get().setActivated(true);
			customer.get().setLocked(false);
			customer.get().setPasswordHash(PasswordUtil.encode(customerNewPasswordDto.getPassword()));
			customer.get().setLastModifiedBy(SecurityUtil.getLogged());
			customer.get().setLastModifiedDate(Instant.now());
			customerRepository.save(customer.get());
		}
	}
	
	@Transactional
	public void taskSave(Customer newCustomer) throws IOException {
		if(newCustomer.getId() == null) {
			FileImageTask fileImageTask = imageService.add(newCustomer.getMultipartFileImage());
			newCustomer.setImage(fileImageTask.getId());
			taskService.saveTaskAdd(DomainConstant.CUSTOMER, toString(newCustomer));
		}else {
			Customer oldCustomer = customerRepository.getOne(newCustomer.getId());
			prepareAddress(oldCustomer);
			
			if(newCustomer.getMultipartFileImage().isEmpty()) {
				newCustomer.setImage(newCustomer.getId());
			}else {
				FileImageTask fileImageTask = imageService.add(newCustomer.getMultipartFileImage());
				newCustomer.setImage(fileImageTask.getId());
			}
			
			newCustomer.setPasswordHash(oldCustomer.getPasswordHash());
			oldCustomer.setImage(newCustomer.getId());
			
			customerRepository.save(true, newCustomer.getId());
			taskService.saveTaskModify(DomainConstant.CUSTOMER, toString(oldCustomer), toString(newCustomer));
		}
	}

	@Transactional
	public void taskDelete(Customer customer) throws JsonProcessingException {
		taskService.saveTaskDelete(DomainConstant.CUSTOMER, toString(customer));
		customerRepository.save(true, customer.getId());
	}
	
	@Transactional
	public String taskConfirm(Task task) throws IOException {
		taskService.confirm(task);
		Customer customer = objectMapper.readValue((task.getTaskOperation() == TaskOperation.DELETE) ? task.getTaskDataOld() : task.getTaskDataNew(), Customer.class);
		Address primary = customer.getPrimary();
		Address secondary = customer.getSecondary();
		
		if(task.getVerified()) {
			if(task.getTaskOperation() == TaskOperation.ADD) {
				customer.setCreatedBy(task.getMaker());
				customer.setCreatedDate(task.getMadeDate());
				customer = customerRepository.save(customer);
				saveAddress(task, primary, customer);
				saveAddress(task, secondary, customer);
				imageService.add(customer.getId(), customer.getImage(), task.getMaker(), task.getMadeDate());
			}else if(task.getTaskOperation() == TaskOperation.MODIFY) {
				customer.setLastModifiedBy(task.getMaker());
				customer.setLastModifiedDate(task.getMadeDate());
				customer.setPending(false);
				customer = customerRepository.save(customer);
				saveAddress(task, primary, customer);
				saveAddress(task, secondary, customer);
				imageService.update(customer.getId(), customer.getImage(), task.getMaker(), task.getMadeDate());
			}else if(task.getTaskOperation() == TaskOperation.DELETE) {
				customerRepository.delete(customer);
				imageService.delete(customer.getId());
			}
		}else if(task.getTaskOperation() != TaskOperation.ADD) {
			customerRepository.save(false, customer.getId());
		}

		return customer.getEmail();
	}

	private void saveAddress(Task task, Address address, Customer customer) {
		if(address != null) {
			address.setCustomer(customer);
			if(task.getTaskOperation() == TaskOperation.ADD) {
				address.setCreatedBy(task.getMaker());
				address.setCreatedDate(task.getMadeDate());
				addressRepository.save(address);
			}else if(task.getTaskOperation() == TaskOperation.MODIFY) {
				address.setLastModifiedBy(task.getMaker());
				address.setLastModifiedDate(task.getMadeDate());
				addressRepository.save(address);
			}else if(task.getTaskOperation() == TaskOperation.DELETE) {
				addressRepository.delete(address);
			}
		}
	}

	@Transactional(propagation=Propagation.REQUIRES_NEW)
	public String taskFailed(Exception ex, Task task) {
		try {
			Customer customer = toCustomer((task.getTaskOperation() == TaskOperation.DELETE) ? task.getTaskDataOld() : task.getTaskDataNew());
			
			if(task.getTaskOperation() != TaskOperation.ADD) {
				customerRepository.save(false, customer.getId());
			}

			taskService.save(ex, task);
			return ex.getLocalizedMessage();
		} catch (Exception e) {
			return e.getLocalizedMessage();
		}
	}
	
	public void prepareAddress(Customer customer) {
		for(Address address: customer.getAddresses()) {
			if(address.getLabel().equals(AddressConstant.PRIMARY)) {
				customer.setPrimary(address);
			}else if(address.getLabel().equals(AddressConstant.SECONDARY)) {
				customer.setSecondary(address);
			}
		}
		
		customer.setAddresses(null);
	}
	
	private String toString(Customer customer) throws JsonProcessingException {
		return objectMapper.writeValueAsString(customer);
	}
	
	private Customer toCustomer(String content) throws IOException {
		return objectMapper.readValue(content, Customer.class);
	}
	
}
