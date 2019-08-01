package com.dbs.loyalty.web.validator.rest;

import java.util.Optional;

import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.dbs.loyalty.domain.TrxOrder;
import com.dbs.loyalty.domain.TrxProduct;
import com.dbs.loyalty.service.TrxProductService;
import com.dbs.loyalty.util.ErrorUtil;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class TrxOrderValidator implements Validator {
	
	private static final String ITEM_ID = "itemId";
	
	private static final String ITEM_POINT = "itemPoint";
	
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
			message = ErrorUtil.getNotFoundMessage(ErrorUtil.PRODUCT, trxOrder.getItemId());
			errors.rejectValue(ITEM_ID, message, message);
		}else if(trxOrder.getItemPoint().intValue() != trxProduct.get().getPoint().intValue()) {
			message = ErrorUtil.getIvalidDataMessage(ITEM_POINT, String.valueOf(trxProduct.get().getPoint()));
			errors.rejectValue(ITEM_POINT, message, message);
		}
	}

}
