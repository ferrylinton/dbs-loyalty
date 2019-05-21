package com.dbs.loyalty.service;

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
	
}
