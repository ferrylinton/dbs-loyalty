package com.dbs.loyalty.service;

import java.util.Map;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.dbs.loyalty.domain.Province;
import com.dbs.loyalty.repository.ProvinceRepository;
import com.dbs.loyalty.service.specification.ProvinceSpec;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class ProvinceService{

	private final ProvinceRepository provinceRepository;

	public Optional<Province> findById(Integer id){
		return provinceRepository.findById(id);
	}
	
	public Page<Province> findAll(Map<String, String> params, Pageable pageable) {
		return provinceRepository.findAll(new ProvinceSpec(params), pageable);
	}
	
}
