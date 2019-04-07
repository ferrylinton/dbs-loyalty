package com.dbs.loyalty.service;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.dbs.loyalty.repository.CustomerImageRepository;
import com.dbs.loyalty.service.dto.CustomerImageDto;
import com.dbs.loyalty.service.mapper.CustomerImageMapper;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class CustomerImageService{

	private final CustomerImageRepository customerImageRepository;
	
	private final CustomerImageMapper customerImageMapper;

	public Optional<CustomerImageDto> findByEmail(String email){
		return customerImageRepository
				.findById(email)
				.map(customerImageMapper::toDto);
	}
}
