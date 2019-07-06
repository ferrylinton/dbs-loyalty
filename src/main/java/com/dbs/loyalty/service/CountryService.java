package com.dbs.loyalty.service;

import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.dbs.loyalty.config.constant.DomainConstant;
import com.dbs.loyalty.domain.Country;
import com.dbs.loyalty.repository.CountryRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class CountryService {

	public static final Sort SORT_BY = Sort.by(DomainConstant.NAME);
	
	private final CountryRepository countryRepository;
	
	public List<Country> findAll(){
		return countryRepository.findAll(SORT_BY);
	}
	
}
