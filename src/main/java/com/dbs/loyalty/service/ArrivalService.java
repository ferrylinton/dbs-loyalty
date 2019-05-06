package com.dbs.loyalty.service;

import java.time.Instant;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.dbs.loyalty.domain.Arrival;
import com.dbs.loyalty.domain.Customer;
import com.dbs.loyalty.exception.BadRequestException;
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

	public Response save(Arrival arrival) throws BadRequestException{
		Optional<Customer> customer = customerRespository.findById(SecurityUtil.getId());
		
		if(customer.isPresent()) {
			Optional<Arrival> current = arrivalRepository.findByCustomerAndAirportAndDate(
					SecurityUtil.getId(), 
					arrival.getAirport().getId(), 
					arrival.getFlightDate(),
					arrival.getFlightTime());
			
			if(current.isPresent()) {
				return new Response("Data is already exist");
			}else {
				arrival.setCustomer(customer.get());
				arrival.setCreatedBy(SecurityUtil.getLogged());
				arrival.setCreatedDate(Instant.now());
				arrival = arrivalRepository.save(arrival);
				return new Response("Data is saved");
			}
		}

		throw new BadRequestException("Failed to save data");
	}
	
}
