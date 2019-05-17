package com.dbs.loyalty.service;

import org.springframework.stereotype.Service;

import com.dbs.loyalty.domain.PriviledgeOrder;
import com.dbs.loyalty.repository.PriviledgeOrderRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class PriviledgeOrderService{

	private final PriviledgeOrderRepository priviledgeOrderRepository;

	public PriviledgeOrder save(PriviledgeOrder priviledgeOrder){
		return priviledgeOrderRepository.save(priviledgeOrder);
	}
	
}
