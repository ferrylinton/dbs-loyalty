package com.dbs.loyalty.web.validator;

import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.dbs.loyalty.domain.PromoCategory;
import com.dbs.loyalty.service.PromoCategoryService;
import com.dbs.loyalty.util.MessageUtil;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class PromoCategoryValidator implements Validator {

	private String validationExistName = "validation.exist.name";

	private String name = "name";

	private final PromoCategoryService promoCategoryService;

	@Override
	public boolean supports(Class<?> clazz) {
		return PromoCategory.class.equals(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		PromoCategory promoCategoryDto = (PromoCategory) target;

		if (promoCategoryService.isNameExist(promoCategoryDto)) {
			Object[] errorArgs = new String[] { promoCategoryDto.getName() };
			String defaultMessage = MessageUtil.getMessage(validationExistName, errorArgs);
			errors.rejectValue(name, validationExistName, errorArgs, defaultMessage);
		}

	}

}
