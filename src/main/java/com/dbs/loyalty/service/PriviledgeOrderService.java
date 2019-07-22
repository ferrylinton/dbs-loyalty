package com.dbs.loyalty.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dbs.loyalty.domain.Customer;
import com.dbs.loyalty.domain.PriviledgeOrder;
import com.dbs.loyalty.domain.Reward;
import com.dbs.loyalty.exception.BadRequestException;
import com.dbs.loyalty.repository.PriviledgeOrderRepository;
import com.dbs.loyalty.util.SecurityUtil;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class PriviledgeOrderService{

	private final PriviledgeOrderRepository priviledgeOrderRepository;
	
	private final AirportAssistanceService airportAssistanceService;
	
	private final RewardService rewardService;
	
	private final CustomerService customerService;

	public Optional<PriviledgeOrder> findById(String id){
		return priviledgeOrderRepository.findById(id);
	}
	
	@Transactional
	public PriviledgeOrder save(PriviledgeOrder priviledgeOrder) throws BadRequestException{
		List<Reward> rewards = rewardService.findAllValid();
		int availablePoints = rewardService.getAvailablePoints(rewards);
		int orderPoints = priviledgeOrder.getItemQuantity() * priviledgeOrder.getItemPoint();
		
		if(availablePoints > orderPoints) {
			Optional<Customer> customer = customerService.findById(SecurityUtil.getId());
			if(customer.isPresent()) {
				priviledgeOrder.setCustomer(customer.get());
				
				rewardService.deduct(customer.get().getEmail(), rewards, orderPoints);
				airportAssistanceService.add(priviledgeOrder.getItemQuantity());
				return priviledgeOrderRepository.save(priviledgeOrder);
			}else{
				throw new BadRequestException("Invalid customer data");
			}
		}else {
			throw new BadRequestException("Insufficient points");
		}
	}
	
}
