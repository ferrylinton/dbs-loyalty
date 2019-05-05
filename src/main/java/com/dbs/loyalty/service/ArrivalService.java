package com.dbs.loyalty.service;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.dbs.loyalty.domain.Arrival;
import com.dbs.loyalty.repository.ArrivalRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class ArrivalService {

	private final ArrivalRepository arrivalRepository;
	
	public Optional<Arrival> findById(String id){
		return arrivalRepository.findById(id);
	}
	
	public Page<Arrival> findAll(Pageable pageable){
		return arrivalRepository.findAll(pageable);
	}

}
