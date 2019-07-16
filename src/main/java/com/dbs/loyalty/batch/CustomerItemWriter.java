package com.dbs.loyalty.batch;

import java.util.List;

import org.springframework.batch.item.ItemWriter;

import com.dbs.loyalty.domain.cst.Customer;
import com.dbs.loyalty.repository.CustomerRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class CustomerItemWriter implements ItemWriter<Customer>{

	private final CustomerRepository customerRepository;
	
	@Override
	public void write(List<? extends Customer> customers) throws Exception {
		customerRepository.saveAll(customers);
	}

}
