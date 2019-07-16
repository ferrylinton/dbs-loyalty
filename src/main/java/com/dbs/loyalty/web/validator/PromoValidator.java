package com.dbs.loyalty.web.validator;

import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.dbs.loyalty.config.constant.DomainConstant;
import com.dbs.loyalty.config.constant.ValidationConstant;
import com.dbs.loyalty.domain.prm.Promo;
import com.dbs.loyalty.service.PromoService;
import com.dbs.loyalty.util.MessageUtil;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class PromoValidator implements Validator {

	private final PromoService promoService;

	@Override
	public boolean supports(Class<?> clazz) {
		return Promo.class.equals(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		Promo promo = (Promo) target;

		if (promoService.isCodeExist(promo.getId(), promo.getCode())) {
			Object[] errorArgs = new String[] { promo.getCode() };
			String defaultMessage = MessageUtil.getMessage(ValidationConstant.VALIDATION_EXIST, errorArgs);
			errors.rejectValue(DomainConstant.CODE, ValidationConstant.VALIDATION_EXIST, errorArgs, defaultMessage);
		}
		
		if (promoService.isTitleExist(promo.getId(), promo.getTitle())) {
			Object[] errorArgs = new String[] { promo.getCode() };
			String defaultMessage = MessageUtil.getMessage(ValidationConstant.VALIDATION_EXIST, errorArgs);
			errors.rejectValue(DomainConstant.TITLE, ValidationConstant.VALIDATION_EXIST, errorArgs, defaultMessage);
		}

		if (promo.getId() == null && promo.getMultipartFileImage().isEmpty()){
			String defaultMessage = MessageUtil.getMessage(ValidationConstant.VALIDATION_EMPTY_FILE);
			errors.rejectValue(DomainConstant.MULTIPART_FILE_IMAGE, ValidationConstant.VALIDATION_EMPTY_FILE, defaultMessage);
        }
		
	}

}
