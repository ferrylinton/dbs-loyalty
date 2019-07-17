package com.dbs.loyalty.service;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.dbs.loyalty.domain.City;
import com.dbs.loyalty.repository.CityRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class CityService {

	private final CityRepository cityRepository;

	public Optional<City> find(Integer id){
		return cityRepository.findById(id);
	}
	
	public Optional<City> findByNameIgnoreCase(String name){
		return cityRepository.findByNameIgnoreCase(name);
	}
	
}
