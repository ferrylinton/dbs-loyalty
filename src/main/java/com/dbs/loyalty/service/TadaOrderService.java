package com.dbs.loyalty.service;

import java.util.Optional;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.stereotype.Service;

import com.dbs.loyalty.domain.TadaOrder;
import com.dbs.loyalty.repository.TadaOrderRepository;
import com.dbs.loyalty.util.SecurityUtil;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class TadaOrderService{
	
	private static final String PREFIX = "DBS-";

	private final TadaOrderRepository tadaOrderRepository;

	public Optional<TadaOrder> findByOrderReference(String orderReference){
		return tadaOrderRepository.findByOrderReference(orderReference);
	}
	
	public Optional<TadaOrder> findByOrderReferenceAndCreatedBy(String orderReference, String createdBy){
		return tadaOrderRepository.findByOrderReferenceAndCreatedBy(orderReference, createdBy);
	}
	
	public String generate() {
		String orderReference = PREFIX + RandomStringUtils.randomNumeric(10);
		Optional<TadaOrder> current = tadaOrderRepository.findByPendingAndCreatedBy(true, SecurityUtil.getLogged());
		
		if(current.isPresent()) {
			return current.get().getOrderReference();
		}else {
			tadaOrderRepository.findByOrderReference(orderReference);
			
			if(current.isPresent()) {
				orderReference = PREFIX + RandomStringUtils.randomNumeric(10);
				current = tadaOrderRepository.findByOrderReference(orderReference);
				
				if(current.isPresent()) {
					orderReference = PREFIX + RandomStringUtils.randomNumeric(10);
					current = tadaOrderRepository.findByOrderReference(orderReference);
					
					if(current.isPresent()) {
						return null;
					}
				}
			}
			
			TadaOrder newTadaOrder = new TadaOrder();
			newTadaOrder.setOrderReference(orderReference);
			newTadaOrder.setCreatedBy(SecurityUtil.getLogged());
			newTadaOrder.setPending(true);
			tadaOrderRepository.save(newTadaOrder);
			return orderReference;
		}
	}
	
	public TadaOrder save(TadaOrder tadaOrder) {
		return tadaOrderRepository.save(tadaOrder);
	}
	
}