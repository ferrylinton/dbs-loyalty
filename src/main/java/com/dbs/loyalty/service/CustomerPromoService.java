package com.dbs.loyalty.service;

import static com.dbs.loyalty.config.constant.MessageConstant.DATA_WITH_VALUE_NOT_FOUND;

import java.time.Instant;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.dbs.loyalty.domain.Customer;
import com.dbs.loyalty.domain.CustomerPromo;
import com.dbs.loyalty.domain.CustomerPromoId;
import com.dbs.loyalty.domain.Promo;
import com.dbs.loyalty.exception.NotFoundException;
import com.dbs.loyalty.repository.CustomerPromoRepository;
import com.dbs.loyalty.repository.CustomerRepository;
import com.dbs.loyalty.repository.PromoRepository;
import com.dbs.loyalty.util.MessageUtil;
import com.dbs.loyalty.util.SecurityUtil;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class CustomerPromoService{

	private final CustomerRepository customerRepository;
	
	private final PromoRepository promoRepository;
	
	private final CustomerPromoRepository customerPromoRepository;

	public CustomerPromo save(String promoId) throws NotFoundException {
		Optional<Customer> customer = customerRepository.findByEmail(SecurityUtil.getLogged());
		Optional<Promo> promo = promoRepository.findById(promoId);
		String message = null;
		
		if(!customer.isPresent()) {
			message = MessageUtil.getMessage(DATA_WITH_VALUE_NOT_FOUND, SecurityUtil.getLogged());
			throw new NotFoundException(message);
		}else if(!promo.isPresent()) {
			message = MessageUtil.getMessage(DATA_WITH_VALUE_NOT_FOUND, promoId);
			throw new NotFoundException(message);
		}else {
			CustomerPromoId id = new CustomerPromoId(customer.get().getId(), promoId);
			Optional<CustomerPromo> current = customerPromoRepository.findById(id);
			
			if(current.isPresent()) {
				return current.get();
			}else {
				CustomerPromo customerPromo = new CustomerPromo();
				customerPromo.setId(id);
				customerPromo.setCustomer(customer.get());
				customerPromo.setPromo(promo.get());
				customerPromo.setCreatedBy(SecurityUtil.getLogged());
				customerPromo.setCreatedDate(Instant.now());
				customerPromo = customerPromoRepository.save(customerPromo);
				
				return customerPromo;
			}
		}
	}
	
}
