package com.dbs.loyalty.web.validator.rest;

import java.util.Optional;

import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.dbs.loyalty.domain.PriviledgeOrder;
import com.dbs.loyalty.domain.PriviledgeProduct;
import com.dbs.loyalty.service.PriviledgeProductService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class PriviledgeOrderValidator implements Validator {
	
	private final PriviledgeProductService priviledgeProductService;

	@Override
	public boolean supports(Class<?> clazz) {
		return PriviledgeOrder.class.equals(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		PriviledgeOrder priviledgeOrder = (PriviledgeOrder) target;
		String message;
		
		Optional<PriviledgeProduct> priviledgeProduct = priviledgeProductService.findById(priviledgeOrder.getItemId());
		
		if(!priviledgeProduct.isPresent()) {
			message = String.format("Product [id=%s] is not found", priviledgeOrder.getItemId());
			errors.rejectValue("message", message, message);
		}
	}

}
