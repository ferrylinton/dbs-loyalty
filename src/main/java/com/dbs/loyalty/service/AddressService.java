package com.dbs.loyalty.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.dbs.loyalty.domain.Address;
import com.dbs.loyalty.repository.AddressRepository;

import lombok.RequiredArgsConstructor;


@RequiredArgsConstructor
@Service
public class AddressService {

	private final AddressRepository addressRepository;

	public List<Address> findByCustomerEmail(String customerEmail){
		return addressRepository.findByCustomerEmail(customerEmail);
	}
	
	public Address save(Address address) {
		return addressRepository.save(address);
	}
	
}
