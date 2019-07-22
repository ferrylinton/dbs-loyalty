package com.dbs.loyalty.service;

import java.time.Instant;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.dbs.loyalty.domain.AirportAssistance;
import com.dbs.loyalty.repository.AirportAssistanceRepository;
import com.dbs.loyalty.util.SecurityUtil;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class AirportAssistanceService {

	private final AirportAssistanceRepository airportAssistanceRepository;
	
	public Optional<AirportAssistance> findById() {
		return airportAssistanceRepository.findById(SecurityUtil.getId());
	}
	
	public void add(Integer quantity) {
		Optional<AirportAssistance> current = airportAssistanceRepository.findById(SecurityUtil.getId());
		AirportAssistance airportAssistance;
		
		if(current.isPresent()) {
			airportAssistance = current.get();
			airportAssistance.setTotal(airportAssistance.getTotal() + quantity);
			airportAssistance.setLastModifiedBy(SecurityUtil.getLogged());
			airportAssistance.setLastModifiedDate(Instant.now());
		}else {
			airportAssistance = new AirportAssistance();
			airportAssistance.setTotal(airportAssistance.getTotal() + quantity);
			airportAssistance.setCreatedBy(SecurityUtil.getLogged());
			airportAssistance.setCreatedDate(Instant.now());
		}
		airportAssistanceRepository.save(airportAssistance);
	}
	
}
