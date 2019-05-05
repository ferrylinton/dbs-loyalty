package com.dbs.loyalty.service;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.dbs.loyalty.domain.AirportAssistance;
import com.dbs.loyalty.domain.enumeration.FlightType;
import com.dbs.loyalty.repository.AirportAssistanceRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class AirportAssistanceService {

	private final AirportAssistanceRepository airportAssistanceRepository;
	
	public Optional<AirportAssistance> findById(String id){
		return airportAssistanceRepository.findById(id);
	}
	
	public Page<AirportAssistance> findByFlightType(FlightType flightType, Pageable pageable){
		return airportAssistanceRepository.findByFlightType(flightType, pageable);
	}

}
