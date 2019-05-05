package com.dbs.loyalty.service;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.dbs.loyalty.domain.Departure;
import com.dbs.loyalty.repository.DepartureRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class DepartureService {

	private final DepartureRepository departureRepository;
	
	public Optional<Departure> findById(String id){
		return departureRepository.findById(id);
	}
	
	public Page<Departure> findAll(Pageable pageable){
		return departureRepository.findAll(pageable);
	}

}
