package com.dbs.loyalty.batch;

import java.time.Instant;
import java.util.Optional;

import org.springframework.batch.item.ItemProcessor;

import com.dbs.loyalty.config.constant.Constant;
import com.dbs.loyalty.domain.Customer;
import com.dbs.loyalty.repository.CustomerRepository;
import com.devskiller.friendly_id.FriendlyId;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class CustomerItemProcessor implements ItemProcessor<CustomerItem, CustomerItem> {
	
	private final CustomerRepository customerRepository;
	
	@Override
	public CustomerItem process(CustomerItem customer) throws Exception {
		Optional<Customer> current = customerRepository.findByNameIgnoreCaseOrEmailIgnoreCase(customer.getName(), customer.getEmail());
		
		if(current.isPresent()) {
			customer.setId(current.get().getId());
			customer.setEmail(current.get().getEmail());
			customer.setName(current.get().getName());
			customer.setPhone(current.get().getPhone());
			customer.setLastModifiedBy(Constant.SYSTEM);
			customer.setLastModifiedDate(Instant.now());
		}else {
			customer.setId(FriendlyId.createFriendlyId());
			customer.setLocked(false);
			customer.setActivated(true);
			customer.setCreatedBy(Constant.SYSTEM);
			customer.setCreatedDate(Instant.now());
		}
		
		
		return customer;
	}

}
