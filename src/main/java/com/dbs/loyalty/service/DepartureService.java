package com.dbs.loyalty.service;

import java.time.Instant;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.dbs.loyalty.domain.Customer;
import com.dbs.loyalty.domain.Departure;
import com.dbs.loyalty.exception.BadRequestException;
import com.dbs.loyalty.repository.CustomerRepository;
import com.dbs.loyalty.repository.DepartureRepository;
import com.dbs.loyalty.util.SecurityUtil;
import com.dbs.loyalty.web.response.Response;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class DepartureService {

	private final DepartureRepository departureRepository;
	
	private final CustomerRepository customerRespository;
	
	public Optional<Departure> findById(String id){
		return departureRepository.findById(id);
	}
	
	public Page<Departure> findAll(Pageable pageable){
		return departureRepository.findAll(pageable);
	}

	public Response save(Departure departure) throws BadRequestException{
		Optional<Customer> customer = customerRespository.findById(SecurityUtil.getId());
		
		if(customer.isPresent()) {
			Optional<Departure> current = departureRepository.findByCustomerAndAirportAndDate(
					SecurityUtil.getId(), 
					departure.getAirport().getId(), 
					departure.getFlightDate(),
					departure.getFlightTime());
			
			if(current.isPresent()) {
				return new Response("Data is already exist");
			}else {
				departure.setCustomer(customer.get());
				departure.setCreatedBy(SecurityUtil.getLogged());
				departure.setCreatedDate(Instant.now());
				departure = departureRepository.save(departure);
				return new Response("Data is saved");
			}
		}
		
		throw new BadRequestException("Failed to save data");
	}
	
}
