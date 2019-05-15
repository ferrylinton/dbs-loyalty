package com.dbs.loyalty.batch;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Optional;

import org.springframework.batch.item.ItemProcessor;

import com.dbs.loyalty.config.constant.Constant;
import com.dbs.loyalty.domain.Customer;
import com.dbs.loyalty.repository.CustomerRepository;
import com.devskiller.friendly_id.FriendlyId;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class CustomerItemProcessor implements ItemProcessor<CustomerItem, Customer> {
	
	private final static SimpleDateFormat sdf =  new SimpleDateFormat("yyyy-MM-dd");
	
	private final CustomerRepository customerRepository;
	
	@Override
	public Customer process(CustomerItem customerItem) throws Exception {
		Optional<Customer> current = customerRepository.findByNameIgnoreCaseOrEmailIgnoreCase(customerItem.getName(), customerItem.getEmail());
		Customer customer = new Customer();

		if(current.isPresent()) {
			customer.setId(current.get().getId());
			customer.setEmail(current.get().getEmail());
			customer.setName(current.get().getName());
			customer.setPhone(current.get().getPhone());
			customer.setPasswordHash(current.get().getPasswordHash());
			customer.setCustomerType(customerItem.getCustomerType());
			customer.setDob(sdf.parse(customerItem.getDob()));
			customer.setLastModifiedBy(Constant.SYSTEM);
			customer.setLastModifiedDate(Instant.now());
		}else {
			customer.setId(FriendlyId.createFriendlyId());
			customer.setEmail(customerItem.getEmail());
			customer.setName(customerItem.getName());
			customer.setPhone(customerItem.getPhone());
			customer.setPasswordHash(customerItem.getPasswordHash());
			customer.setCustomerType(customerItem.getCustomerType());
			customer.setDob(sdf.parse(customerItem.getDob()));
			customer.setLocked(false);
			customer.setActivated(true);
			customer.setCreatedBy(Constant.SYSTEM);
			customer.setCreatedDate(Instant.now());
		}
		
		
		return customer;
	}

}
