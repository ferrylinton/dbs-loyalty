package com.dbs.loyalty.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dbs.loyalty.config.constant.MessageConstant;
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
		int availablePoints = rewardService.getAvailablePoints(rewards);
		int orderPoints = trxOrder.getItemPoint();
		
		if(availablePoints > orderPoints) {
			Customer customer = customerService.findLoggedUserByEmail(SecurityUtil.getLogged());
			trxOrder.setCustomer(customer);
			
			rewardService.deduct(customer.getEmail(), rewards, orderPoints);
			rewardService.save(rewards);
			return trxOrderRepository.save(trxOrder);
		}else {
			throw new BadRequestException(MessageConstant.INSUFFICIENT_POINTS);
		}
	}
	
}
