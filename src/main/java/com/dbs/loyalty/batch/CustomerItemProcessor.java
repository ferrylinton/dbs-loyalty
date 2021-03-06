package com.dbs.loyalty.batch;

import java.time.Instant;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

import org.springframework.batch.item.ItemProcessor;

import com.dbs.loyalty.config.constant.Constant;
import com.dbs.loyalty.config.constant.DateConstant;
import com.dbs.loyalty.domain.Customer;
import com.dbs.loyalty.repository.CustomerRepository;
import com.devskiller.friendly_id.FriendlyId;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class CustomerItemProcessor implements ItemProcessor<CustomerItem, Customer> {

	private final CustomerRepository customerRepository;
	
	@Override
	public Customer process(CustomerItem customerItem) throws Exception {
		Optional<Customer> current = customerRepository.findByCif(customerItem.getCif());
		
		if(current.isPresent()) {
			current.get().setCif(customerItem.getCif());
			current.get().setEmail(customerItem.getEmail());
			current.get().setFirstName(customerItem.getFirstName());
			current.get().setLastName(customerItem.getLastName());
			current.get().setPhone(customerItem.getPhone());
			current.get().setCustomerType(customerItem.getCustomerType());
			current.get().setDob(LocalDate.parse(customerItem.getDob(), DateTimeFormatter.ofPattern(DateConstant.JAVA_DATE)));
			current.get().setLastModifiedBy(Constant.SYSTEM);
			current.get().setLastModifiedDate(Instant.now());
			
			return current.get();
		}else {
			Customer customer = new Customer();
			customer.setId(FriendlyId.createFriendlyId());
			customer.setCif(customerItem.getCif());
			customer.setEmail(customerItem.getEmail());
			customer.setFirstName(customerItem.getFirstName());
			customer.setLastName(customerItem.getLastName());
			customer.setPhone(customerItem.getPhone());
			customer.setCustomerType(customerItem.getCustomerType());
			customer.setDob(LocalDate.parse(customerItem.getDob(), DateTimeFormatter.ofPattern(DateConstant.JAVA_DATE)));
			customer.setLocked(false);
			customer.setActivated(false);
			customer.setCreatedBy(Constant.SYSTEM);
			customer.setCreatedDate(Instant.now());
			
			return customer;
		}
	}

}
