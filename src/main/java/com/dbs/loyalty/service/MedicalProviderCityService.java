package com.dbs.loyalty.service;

import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.dbs.loyalty.domain.MedicalProviderCity;
import com.dbs.loyalty.repository.MedicalProviderCityRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class MedicalProviderCityService {

	private Sort sortBy = Sort.by("name");
	
	private final MedicalProviderCityRepository medicalProviderCityRepository;
	
	public List<MedicalProviderCity> findByMedicalProviderId(String medicalProviderId){
		return medicalProviderCityRepository.findByMedicalProviderId(medicalProviderId, sortBy);
	}
	
}
