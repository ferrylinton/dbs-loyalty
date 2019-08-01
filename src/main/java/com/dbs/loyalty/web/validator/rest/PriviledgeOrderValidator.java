package com.dbs.loyalty.web.validator.rest;

import java.util.Optional;

import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.dbs.loyalty.domain.PriviledgeOrder;
import com.dbs.loyalty.domain.PriviledgeProduct;
import com.dbs.loyalty.service.PriviledgeProductService;
import com.dbs.loyalty.util.ErrorUtil;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class PriviledgeOrderValidator implements Validator {
	
	private static final String ITEM_ID = "itemId";
	
	private static final String ITEM_POINT = "itemPoint";
	
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
			message = ErrorUtil.getNotFoundMessage(ErrorUtil.PRODUCT, priviledgeOrder.getItemId());
			errors.rejectValue(ITEM_ID, message, message);
		}else if(priviledgeOrder.getItemPoint().intValue() != priviledgeProduct.get().getPoint().intValue()) {
			message = ErrorUtil.getIvalidDataMessage(ITEM_POINT, String.valueOf(priviledgeProduct.get().getPoint()));
			errors.rejectValue(ITEM_POINT, message, message);
		}
	}

}
