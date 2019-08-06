package com.dbs.loyalty.service;

import java.util.Map;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.dbs.loyalty.domain.Airport;
import com.dbs.loyalty.repository.AirportRepository;
import com.dbs.loyalty.service.specification.AirportSpec;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class AirportService {

	private final AirportRepository airportRepository;
	
	public Optional<Airport> findById(String id){
		return airportRepository.findById(id);
	}

	public Page<Airport> findAll(Map<String, String> params, Pageable pageable) {
		return airportRepository.findAll(new AirportSpec(params), pageable);
	}
	
}
