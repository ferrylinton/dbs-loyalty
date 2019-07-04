package com.dbs.loyalty.service;

import java.time.Instant;
import java.util.Map;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dbs.loyalty.config.constant.MessageConstant;
import com.dbs.loyalty.domain.Arrival;
import com.dbs.loyalty.domain.Customer;
import com.dbs.loyalty.domain.AirportAssistance;
import com.dbs.loyalty.exception.BadRequestException;
import com.dbs.loyalty.repository.ArrivalRepository;
import com.dbs.loyalty.repository.CustomerRepository;
import com.dbs.loyalty.service.specification.ArrivalSpec;
import com.dbs.loyalty.repository.AirportAssistanceRepository;
import com.dbs.loyalty.util.SecurityUtil;
import com.dbs.loyalty.web.response.Response;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class ArrivalService {

	private final ArrivalRepository arrivalRepository;
	
	private final CustomerRepository customerRespository;
	
	private final AirportAssistanceRepository travelAssistanceRepository;
	
	public Optional<Arrival> findById(String id){
		return arrivalRepository.findById(id);
	}
	
	public Page<Arrival> findAll(Map<String, String> params, Pageable pageable){
		return arrivalRepository.findAll(new ArrivalSpec(params), pageable);
	}

	@Transactional
	public Response save(Arrival arrival) throws BadRequestException{
		Optional<Customer> customer = customerRespository.findById(SecurityUtil.getId());
		
		if(customer.isPresent()) {
			Optional<Arrival> current = arrivalRepository.findByCustomerAndAirportAndDate(
					SecurityUtil.getId(), 
					arrival.getAirport().getId(), 
					arrival.getFlightDate());
			
			if(current.isPresent()) {
				return new Response(MessageConstant.DATA_IS_ALREADY_EXIST);
			}else {
				Optional<AirportAssistance> travelAssistance = travelAssistanceRepository.findById(SecurityUtil.getId());
				
				if(travelAssistance.isPresent() && travelAssistance.get().getTotal() > 0) {
					travelAssistance.get().setTotal(travelAssistance.get().getTotal() - 1);
					travelAssistanceRepository.save(travelAssistance.get());
					
					arrival.setCustomer(customer.get());
					arrival.setCreatedBy(SecurityUtil.getLogged());
					arrival.setCreatedDate(Instant.now());
					arrival = arrivalRepository.save(arrival);
					return new Response(MessageConstant.DATA_IS_SAVED);
				}else {
					throw new BadRequestException(MessageConstant.NO_LIMIT_iS_AVAILABLE);
				}
			}
		}

		throw new BadRequestException(MessageConstant.FAILED_TO_SAVE_DATA);
	}
	
}
