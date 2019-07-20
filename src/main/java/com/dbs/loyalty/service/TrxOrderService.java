package com.dbs.loyalty.service;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dbs.loyalty.domain.Customer;
import com.dbs.loyalty.domain.Reward;
import com.dbs.loyalty.domain.TrxOrder;
import com.dbs.loyalty.exception.BadRequestException;
import com.dbs.loyalty.repository.TrxOrderRepository;
import com.dbs.loyalty.util.SecurityUtil;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class TrxOrderService{

	private final TrxOrderRepository trxOrderRepository;
	
	private final RewardService rewardService;
	
	private final CustomerService customerService;

	public Optional<TrxOrder> findById(String id){
		return trxOrderRepository.findById(id);
	}

	public List<TrxOrder> findAll(){
		return trxOrderRepository.findAll();
	}
	
	@Transactional
	public TrxOrder save(TrxOrder trxOrder) throws BadRequestException{
		List<Reward> rewards = rewardService.findAllValid();
		int totalCustomerPoint = rewardService.getTotal(rewards);
		int totalOrderPoint = trxOrder.getItemPoint();
		
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
				trxOrder.setCustomer(customer.get());
				return trxOrderRepository.save(trxOrder);
			}else{
				throw new BadRequestException("Invalid customer data");
			}
		}else {
			throw new BadRequestException("Insufficient points");
		}
	}
	
}
