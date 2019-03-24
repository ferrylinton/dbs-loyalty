package com.dbs.loyalty.web.validator;

import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.dbs.loyalty.service.MessageService;
import com.dbs.loyalty.service.PromoCategoryService;
import com.dbs.loyalty.service.dto.PromoCategoryDto;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class PromoCategoryValidator implements Validator {

	private String NAME_EXIST = "validation.exist.name";

	private String NAME = "name";

	private final PromoCategoryService promoCategoryService;

	@Override
	public boolean supports(Class<?> clazz) {
		return PromoCategoryDto.class.equals(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		PromoCategoryDto promoCategoryDto = (PromoCategoryDto) target;

		if (promoCategoryService.isNameExist(promoCategoryDto)) {
			Object[] errorArgs = new String[] { promoCategoryDto.getName() };
			String defaultMessage = MessageService.getMessage(NAME_EXIST, errorArgs);
			errors.rejectValue(NAME, NAME_EXIST, errorArgs, defaultMessage);
		}

	}

}
