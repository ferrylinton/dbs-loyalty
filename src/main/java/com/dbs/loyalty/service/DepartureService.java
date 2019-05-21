package com.dbs.loyalty.service;

import java.time.Instant;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dbs.loyalty.domain.Customer;
import com.dbs.loyalty.domain.Departure;
import com.dbs.loyalty.domain.AirportAssistance;
import com.dbs.loyalty.exception.BadRequestException;
import com.dbs.loyalty.repository.CustomerRepository;
import com.dbs.loyalty.repository.DepartureRepository;
import com.dbs.loyalty.repository.AirportAssistanceRepository;
import com.dbs.loyalty.util.SecurityUtil;
import com.dbs.loyalty.web.response.Response;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class DepartureService {

	private final DepartureRepository departureRepository;
	
	private final CustomerRepository customerRespository;
	
	private final AirportAssistanceRepository travelAssistanceRepository;
	
	public Optional<Departure> findById(String id){
		return departureRepository.findById(id);
	}
	
	public Page<Departure> findAll(Pageable pageable){
		return departureRepository.findAll(pageable);
	}

	@Transactional
	public Response save(Departure departure) throws BadRequestException{
		Optional<Customer> customer = customerRespository.findById(SecurityUtil.getId());
		
		if(customer.isPresent()) {
			Optional<Departure> current = departureRepository.findByCustomerAndAirportAndDate(
					SecurityUtil.getId(), 
					departure.getAirport().getId(), 
					departure.getFlightDate());
			
			if(current.isPresent()) {
				return new Response("Data is already exist");
			}else {
				Optional<AirportAssistance> travelAssistance = travelAssistanceRepository.findById(SecurityUtil.getId());
				
				if(travelAssistance.isPresent() && travelAssistance.get().getTotal() > 0) {
					travelAssistance.get().setTotal(travelAssistance.get().getTotal() - 1);
					travelAssistanceRepository.save(travelAssistance.get());
					
					departure.setCustomer(customer.get());
					departure.setCreatedBy(SecurityUtil.getLogged());
					departure.setCreatedDate(Instant.now());
					departure = departureRepository.save(departure);
					return new Response("Data is saved");
				}else {
					throw new BadRequestException("No limit is available");
				}
			}
		}
		
		throw new BadRequestException("Failed to save data");
	}
	
}
