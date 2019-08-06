package com.dbs.loyalty.service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.dbs.loyalty.config.constant.DomainConstant;
import com.dbs.loyalty.domain.MedicalBranch;
import com.dbs.loyalty.repository.MedicalBranchRepository;
import com.dbs.loyalty.service.specification.MedicalBranchSpec;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class MedicalBranchService {

	private Sort sortBy = Sort.by(DomainConstant.ADDRESS);
	
	private final MedicalBranchRepository medicalBranchRepository;
	
	public Optional<MedicalBranch> findById(String id){
		return medicalBranchRepository.findById(id);
	}
	
	public List<MedicalBranch> findByMedicalCityId(String medicalCityId){
		return medicalBranchRepository.findByMedicalCityId(medicalCityId, sortBy);
	}
	
	public Page<MedicalBranch> findAll(Map<String, String> params, Pageable pageable) {
		return medicalBranchRepository.findAll(new MedicalBranchSpec(params), pageable);
	}
	
}
