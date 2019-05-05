package com.dbs.loyalty.service;

import java.time.Instant;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.dbs.loyalty.domain.Arrival;
import com.dbs.loyalty.domain.Customer;
import com.dbs.loyalty.repository.ArrivalRepository;
import com.dbs.loyalty.repository.CustomerRepository;
import com.dbs.loyalty.util.SecurityUtil;
import com.dbs.loyalty.web.response.Response;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class ArrivalService {

	private final ArrivalRepository arrivalRepository;
	
	private final CustomerRepository customerRespository;
	
	public Optional<Arrival> findById(String id){
		return arrivalRepository.findById(id);
	}
	
	public Page<Arrival> findAll(Pageable pageable){
		return arrivalRepository.findAll(pageable);
	}

	public Response save(Arrival arrival){
		Optional<Customer> customer = customerRespository.findById(SecurityUtil.getId());
		
		if(customer.isPresent()) {
			arrival.setCustomer(customer.get());
		}
		
		arrival.setCreatedBy(SecurityUtil.getLogged());
		arrival.setCreatedDate(Instant.now());
		arrival = arrivalRepository.save(arrival);
		return new Response("Data is saved");
	}
}
