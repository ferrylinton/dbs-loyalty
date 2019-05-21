package com.dbs.loyalty.service;

import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.dbs.loyalty.domain.MedicalProviderBranch;
import com.dbs.loyalty.repository.MedicalProviderBranchRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class MedicalProviderBranchService {

	private Sort sortBy = Sort.by("address");
	
	private final MedicalProviderBranchRepository medicalProviderBranchRepository;
	
	public List<MedicalProviderBranch> findByMedicalProviderCityId(String medicalProviderCityId){
		return medicalProviderBranchRepository.findByMedicalProviderCityId(medicalProviderCityId, sortBy);
	}
	
}
