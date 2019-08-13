package com.dbs.loyalty.service;

import org.springframework.stereotype.Service;

import com.dbs.loyalty.config.ApplicationProperties;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class CustomerTypeService {

	public static final String TPC				= "TPC";
	
	public static final String TREASURE			= "TREASURE";
	
	private final ApplicationProperties applicationProperties;
	
	public String getCustomerType(String value) {
		if(isTpc(value)) {
			return TPC;
		}else {
			return TREASURE;
		}
	}

	public boolean isTpc(String value) {
		return getTpc().equals(value);
	}
	
	public String getTpc() {
		return applicationProperties.getCustomer().getTpc();
	}
	
	public String getTreasure() {
		return applicationProperties.getCustomer().getTreasure();
	}
	
}
