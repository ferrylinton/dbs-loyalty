package com.dbs.loyalty.service;

import java.time.Instant;
import java.util.Map;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.dbs.loyalty.config.constant.Constant;
import com.dbs.loyalty.config.constant.MessageConstant;
import com.dbs.loyalty.domain.Promo;
import com.dbs.loyalty.domain.PromoCustomer;
import com.dbs.loyalty.domain.PromoCustomerId;
import com.dbs.loyalty.exception.NotFoundException;
import com.dbs.loyalty.repository.PromoCustomerRepository;
import com.dbs.loyalty.repository.PromoRepository;
import com.dbs.loyalty.util.MessageUtil;
import com.dbs.loyalty.web.response.Response;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class PromoCustomerService{

	private final PromoRepository promoRepository;
	
	private final PromoCustomerRepository promoCustomerRepository;

	public Response save(String promoId) throws NotFoundException {
		Optional<Promo> promo = promoRepository.findById(promoId);
		
		if(!promo.isPresent()) {
			String message = MessageUtil.getNotFoundMessage(promoId);
			throw new NotFoundException(message);
		}else {
			PromoCustomerId id = new PromoCustomerId(promoId);
			Optional<PromoCustomer> current = promoCustomerRepository.findById(id);
			
			if(current.isPresent()) {
				return new Response(MessageConstant.DATA_IS_ALREADY_EXIST);
			}else {
				PromoCustomer customerPromo = new PromoCustomer();
				customerPromo.setId(id);
				customerPromo.setCreatedDate(Instant.now());
				customerPromo = promoCustomerRepository.save(customerPromo);
				
				return new Response(String.format(MessageConstant.DATA_IS_SAVED, promoId));
			}
		}
	}
	
	public Page<PromoCustomer> findPromoCustomers(String promoId, Map<String, String> params, Pageable pageable){
		if(params.containsKey(Constant.KY_PARAM)) {
			String keyword = String.format(Constant.LIKE_FORMAT, params.get(Constant.KY_PARAM).trim().toLowerCase());
			return promoCustomerRepository.findByPromoIdAndKeyword(promoId, keyword, pageable);
		}else {
			return promoCustomerRepository.findByPromoId(promoId, pageable);
		}
		
	}
	
}
