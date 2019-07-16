package com.dbs.loyalty.service;

import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.dbs.loyalty.domain.MedicalProvider;
import com.dbs.loyalty.repository.MedicalProviderRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class MedicalProviderService {

	private Sort sortByName = Sort.by("name");
	
	private final MedicalProviderRepository medicalProviderRepository;
	
	public List<MedicalProvider> findAll(){
		return medicalProviderRepository.findAll(sortByName);
	}
	
}
