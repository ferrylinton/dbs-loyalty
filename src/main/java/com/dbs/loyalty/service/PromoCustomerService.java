package com.dbs.loyalty.service;

import static com.dbs.loyalty.config.constant.MessageConstant.DATA_WITH_VALUE_NOT_FOUND;

import java.time.Instant;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.dbs.loyalty.domain.Promo;
import com.dbs.loyalty.domain.PromoCustomer;
import com.dbs.loyalty.domain.PromoCustomerId;
import com.dbs.loyalty.exception.NotFoundException;
import com.dbs.loyalty.repository.PromoCustomerRepository;
import com.dbs.loyalty.repository.PromoRepository;
import com.dbs.loyalty.util.MessageUtil;
import com.dbs.loyalty.util.SecurityUtil;
import com.dbs.loyalty.web.response.SuccessResponse;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class PromoCustomerService{

	private final PromoRepository promoRepository;
	
	private final PromoCustomerRepository promoCustomerRepository;

	public SuccessResponse save(String promoId) throws NotFoundException {
		Optional<Promo> promo = promoRepository.findById(promoId);
		
		if(!promo.isPresent()) {
			String message = MessageUtil.getMessage(DATA_WITH_VALUE_NOT_FOUND, SecurityUtil.getLogged());
			throw new NotFoundException(message);
		}else {
			PromoCustomerId id = new PromoCustomerId(SecurityUtil.getId(), promoId);
			Optional<PromoCustomer> current = promoCustomerRepository.findById(id);
			
			if(current.isPresent()) {
				return new SuccessResponse(String.format("Data [%s] is already exist", promoId));
			}else {
				PromoCustomer customerPromo = new PromoCustomer();
				customerPromo.setId(id);
				customerPromo.setCreatedBy(SecurityUtil.getLogged());
				customerPromo.setCreatedDate(Instant.now());
				customerPromo = promoCustomerRepository.save(customerPromo);
				
				return new SuccessResponse(String.format("Data [%s] is saved", promoId));
			}
		}
	}
	
	public Page<PromoCustomer> findWithCustomerByPromoId(String promoId, Pageable pageable){
		return promoCustomerRepository.findWithCustomerByPromoId(promoId, pageable);
	}
	
}
