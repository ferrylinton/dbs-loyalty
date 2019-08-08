package com.dbs.loyalty.service;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.dbs.loyalty.config.constant.DomainConstant;
import com.dbs.loyalty.domain.City;
import com.dbs.loyalty.projection.NameOnly;
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
	
	public List<NameOnly> findByName(String name){
		return cityRepository.findFirst10ByNameContainingIgnoreCase(name, Sort.by(DomainConstant.NAME));
	}
}
