package com.dbs.loyalty.service;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.dbs.loyalty.domain.TravelAssistance;
import com.dbs.loyalty.repository.TravelAssistanceRepository;
import com.dbs.loyalty.util.SecurityUtil;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class TravelAssistanceService {

	private final TravelAssistanceRepository travelAssistanceRepository;
	
	public Optional<TravelAssistance> findById() {
		return travelAssistanceRepository.findById(SecurityUtil.getId());
	}
	
}
