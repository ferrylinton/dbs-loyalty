package com.dbs.loyalty.service;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.dbs.loyalty.domain.Customer;
import com.dbs.loyalty.domain.LovedOne;
import com.dbs.loyalty.repository.CustomerRepository;
import com.dbs.loyalty.repository.LovedOneRepository;
import com.dbs.loyalty.service.dto.LovedOneAddDto;
import com.dbs.loyalty.service.dto.LovedOneDto;
import com.dbs.loyalty.service.dto.LovedOneUpdateDto;
import com.dbs.loyalty.service.mapper.LovedOneMapper;
import com.dbs.loyalty.util.SecurityUtil;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class LovedOneService {

	private final LovedOneRepository lovedOneRepository;
	
	private final CustomerRepository customerRepository;
	
	private final LovedOneMapper lovedOneMapper;
	
	public Optional<LovedOneDto> findById(String id){
		return lovedOneRepository.findById(id).map(lovedOneMapper::toDto);
	}
	
	public boolean isNameExist(LovedOneAddDto lovedOneAddDto) {
		Optional<LovedOne> lovedOne = lovedOneRepository.findByNameIgnoreCaseAndCustomerEmail(lovedOneAddDto.getName(), SecurityUtil.getLogged());
		return lovedOne.isPresent();
	}
	
	public boolean isNameExist(LovedOneUpdateDto lovedOneUpdateDto) {
		Optional<LovedOne> lovedOne = lovedOneRepository.findByNameIgnoreCaseAndCustomerEmail(lovedOneUpdateDto.getName(), SecurityUtil.getLogged());

		if (lovedOne.isPresent()) {
			return (!lovedOneUpdateDto.getId().equals(lovedOne.get().getId()));
		}else {
			return false;
		}
	}
	
	public LovedOneDto add(LovedOneAddDto lovedOneAddDto) {
		Optional<Customer> current = customerRepository.findByEmail(SecurityUtil.getLogged());
		
		if(current.isPresent()) {
			LovedOne lovedOne = lovedOneMapper.toEntity(lovedOneAddDto);
			lovedOne.setCustomer(current.get());
			lovedOne.setCreatedBy(SecurityUtil.getLogged());
			lovedOne.setCreatedDate(Instant.now());
			return lovedOneMapper.toDto(lovedOneRepository.save(lovedOne));
		}else {
			return null;
		}
	}
	
	public LovedOneDto update(LovedOneUpdateDto lovedOneUpdateDto) {
		Optional<Customer> current = customerRepository.findByEmail(SecurityUtil.getLogged());
		
		if(current.isPresent()) {
			LovedOne lovedOne = lovedOneMapper.toEntity(lovedOneUpdateDto);
			lovedOne.setCustomer(current.get());
			lovedOne.setLastModifiedBy(SecurityUtil.getLogged());
			lovedOne.setLastModifiedDate(Instant.now());
			return lovedOneMapper.toDto(lovedOneRepository.save(lovedOne));
		}else {
			return null;
		}
	}
	
	public List<LovedOneDto> findByCustomerEmail(String customerEmail){
		return lovedOneRepository.findByCustomerEmail(customerEmail)
				.stream()
				.map(lovedOneMapper::toDto)
				.collect(Collectors.toList());
	}
	
}
