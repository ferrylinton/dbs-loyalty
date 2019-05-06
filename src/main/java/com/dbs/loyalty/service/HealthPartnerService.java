package com.dbs.loyalty.service;

import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.dbs.loyalty.domain.HealthPartner;
import com.dbs.loyalty.repository.HealthPartnerRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class HealthPartnerService {

	private Sort sortByName = Sort.by("name");
	
	private final HealthPartnerRepository healthPartnerRepository;
	
	public List<HealthPartner> findAll(){
		return healthPartnerRepository.findAll(sortByName);
	}
	
}
