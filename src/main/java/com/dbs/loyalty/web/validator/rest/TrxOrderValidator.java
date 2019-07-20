package com.dbs.loyalty.web.validator.rest;

import java.util.Optional;

import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.dbs.loyalty.domain.TrxOrder;
import com.dbs.loyalty.domain.TrxProduct;
import com.dbs.loyalty.service.TrxProductService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class TrxOrderValidator implements Validator {
	
	private final TrxProductService trxProductService;

	@Override
	public boolean supports(Class<?> clazz) {
		return TrxOrder.class.equals(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		TrxOrder trxOrder = (TrxOrder) target;
		String message;
		
		Optional<TrxProduct> trxProduct = trxProductService.findById(trxOrder.getItemId());
		
		if(!trxProduct.isPresent()) {
			message = String.format("Product [id=%s] is not found", trxOrder.getItemId());
			errors.rejectValue("message", message, message);
		}
	}

}
