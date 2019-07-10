package com.dbs.loyalty.service;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dbs.loyalty.domain.AirportAssistance;
import com.dbs.loyalty.domain.Customer;
import com.dbs.loyalty.domain.PriviledgeOrder;
import com.dbs.loyalty.domain.Reward;
import com.dbs.loyalty.exception.BadRequestException;
import com.dbs.loyalty.repository.AirportAssistanceRepository;
import com.dbs.loyalty.repository.PriviledgeOrderRepository;
import com.dbs.loyalty.util.SecurityUtil;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class PriviledgeOrderService{

	private final PriviledgeOrderRepository priviledgeOrderRepository;
	
	private final AirportAssistanceRepository airportAssistanceRepository;
	
	private final RewardService rewardService;
	
	private final CustomerService customerService;

	public Optional<PriviledgeOrder> findById(String id){
		return priviledgeOrderRepository.findById(id);
	}
	
	@Transactional
	public PriviledgeOrder save(PriviledgeOrder priviledgeOrder) throws BadRequestException{
		List<Reward> rewards = rewardService.findAllValid();
		int totalCustomerPoint = rewardService.getTotal(rewards);
		int totalOrderPoint = priviledgeOrder.getItemQuantity() * priviledgeOrder.getItemPoint();
		
		if(totalCustomerPoint > totalOrderPoint) {
			Optional<Customer> customer = customerService.findById(SecurityUtil.getId());
			if(customer.isPresent()) {
				for(Reward reward : rewards) {
					if(totalOrderPoint > 0) {
						if(totalOrderPoint > reward.getPoint()) {
							totalOrderPoint = totalOrderPoint - reward.getPoint();
							reward.setPoint(0);
						}else{
							reward.setPoint(reward.getPoint() - totalOrderPoint);
							totalOrderPoint = 0;
						}
						
						reward.setLastModifiedBy(SecurityUtil.getLogged());
						reward.setLastModifiedDate(Instant.now());
					}
				}
				rewardService.save(rewards);
				
				Optional<AirportAssistance> current = airportAssistanceRepository.findById(SecurityUtil.getId());
				AirportAssistance airportAssistance;
				
				if(current.isPresent()) {
					airportAssistance = current.get();
					airportAssistance.setTotal(airportAssistance.getTotal() + priviledgeOrder.getItemQuantity());
					airportAssistance.setLastModifiedBy(SecurityUtil.getLogged());
					airportAssistance.setLastModifiedDate(Instant.now());
				}else {
					airportAssistance = new AirportAssistance();
					airportAssistance.setTotal(airportAssistance.getTotal() + priviledgeOrder.getItemQuantity());
					airportAssistance.setCreatedBy(SecurityUtil.getLogged());
					airportAssistance.setCreatedDate(Instant.now());
				}
				airportAssistanceRepository.save(airportAssistance);
				
				priviledgeOrder.setCustomer(customer.get());
				return priviledgeOrderRepository.save(priviledgeOrder);
			}else{
				throw new BadRequestException("Invalid customer data");
			}
		}else {
			throw new BadRequestException("Insufficient points");
		}
	}
	
}
