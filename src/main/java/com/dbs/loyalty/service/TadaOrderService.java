package com.dbs.loyalty.service;

import java.util.Map;
import java.util.Optional;

import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.dbs.loyalty.domain.TadaItem;
import com.dbs.loyalty.domain.TadaOrder;
import com.dbs.loyalty.repository.TadaOrderRepository;
import com.dbs.loyalty.service.specification.TadaOrderSpec;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class TadaOrderService{

	private final TadaOrderRepository tadaOrderRepository;

	public Optional<TadaOrder> findByOrderReference(String orderReference){
		return tadaOrderRepository.findByOrderReference(orderReference);
	}
	
	public Optional<TadaOrder> findByOrderReferenceAndCreatedBy(String orderReference, String createdBy){
		return tadaOrderRepository.findByOrderReferenceAndCreatedBy(orderReference, createdBy);
	}
	
	public Optional<TadaOrder> findById(String id){
		return tadaOrderRepository.findById(id);
	}
	
	public Page<TadaOrder> findAll(Map<String, String> params, Pageable pageable) {
		return tadaOrderRepository.findAll(new TadaOrderSpec(params), pageable);
	}
	
	public TadaOrder save(TadaOrder tadaOrder) {
		for(TadaItem tadaItem : tadaOrder.getTadaItems()) {
			tadaItem.setDescription(StringUtils.truncate(tadaItem.getDescription(), 250));
			tadaItem.setTadaOrder(tadaOrder);
		}
		tadaOrder.getTadaPayment().setTadaOrder(tadaOrder);
		tadaOrder.getTadaRecipient().setTadaOrder(tadaOrder);
		return tadaOrderRepository.save(tadaOrder);
	}
	
}
