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
	
	public LovedOneDto add(LovedOneAddDto lovedOneAddDto) {
		Optional<Customer> customer = customerRepository.findByEmail(SecurityUtil.getCurrentEmail());
		LovedOne lovedOne = lovedOneMapper.toEntity(lovedOneAddDto);
		lovedOne.setCustomer(customer.get());
		lovedOne.setCreatedBy(SecurityUtil.getCurrentEmail());
		lovedOne.setCreatedDate(Instant.now());
		return lovedOneMapper.toDto(lovedOneRepository.save(lovedOne));
	}
	
	public LovedOneDto update(LovedOneUpdateDto lovedOneUpdateDto) {
		Optional<Customer> customer = customerRepository.findByEmail(SecurityUtil.getCurrentEmail());
		LovedOne lovedOne = lovedOneMapper.toEntity(lovedOneUpdateDto);
		lovedOne.setCustomer(customer.get());
		lovedOne.setLastModifiedBy(SecurityUtil.getCurrentEmail());
		lovedOne.setLastModifiedDate(Instant.now());
		return lovedOneMapper.toDto(lovedOneRepository.save(lovedOne));
	}
	
	public List<LovedOneDto> findByCustomerEmail(String customerEmail){
		return lovedOneRepository.findByCustomerEmail(customerEmail)
				.stream()
				.map(lovedOneMapper::toDto)
				.collect(Collectors.toList());
	}
	
}
