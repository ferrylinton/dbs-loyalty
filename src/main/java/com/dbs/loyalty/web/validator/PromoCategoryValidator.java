package com.dbs.loyalty.web.validator;

import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.dbs.loyalty.domain.PromoCategory;
import com.dbs.loyalty.service.MessageService;
import com.dbs.loyalty.service.PromoCategoryService;

public class PromoCategoryValidator implements Validator {

	private final String NAME_EXIST = "validation.exist.name";

	private final String NAME = "name";

	private final PromoCategoryService promoCategoryService;

	public PromoCategoryValidator(final PromoCategoryService promoCategoryService) {
		this.promoCategoryService = promoCategoryService;
	}

	@Override
	public boolean supports(Class<?> clazz) {
		return PromoCategory.class.equals(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		PromoCategory promoCategory = (PromoCategory) target;

		if (promoCategoryService.isNameExist(promoCategory)) {
			Object[] errorArgs = new String[] { promoCategory.getName() };
			String defaultMessage = MessageService.getMessage(NAME_EXIST, errorArgs);
			errors.rejectValue(NAME, NAME_EXIST, errorArgs, defaultMessage);
		}

	}

}
