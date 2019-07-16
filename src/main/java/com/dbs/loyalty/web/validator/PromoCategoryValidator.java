package com.dbs.loyalty.web.validator;

import static com.dbs.loyalty.config.constant.ValidationConstant.VALIDATION_EXIST;

import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.dbs.loyalty.config.constant.DomainConstant;
import com.dbs.loyalty.domain.PromoCategory;
import com.dbs.loyalty.service.PromoCategoryService;
import com.dbs.loyalty.util.MessageUtil;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class PromoCategoryValidator implements Validator {

	private final PromoCategoryService promoCategoryService;

	@Override
	public boolean supports(Class<?> clazz) {
		return PromoCategory.class.equals(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		PromoCategory promoCategory = (PromoCategory) target;

		if (promoCategoryService.isNameExist(promoCategory.getId(), promoCategory.getName())) {
			Object[] errorArgs = new String[] { promoCategory.getName() };
			String defaultMessage = MessageUtil.getMessage(VALIDATION_EXIST, errorArgs);
			errors.rejectValue(DomainConstant.NAME, VALIDATION_EXIST, errorArgs, defaultMessage);
		}

	}

}
