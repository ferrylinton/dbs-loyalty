package com.dbs.loyalty.web.validator;

import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.dbs.loyalty.service.LovedOneService;
import com.dbs.loyalty.service.MessageService;
import com.dbs.loyalty.service.dto.LovedOneUpdateDto;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class LovedOneUpdateValidator implements Validator {

	private String validationExistName = "validation.exist.name";

	private String name = "name";

	private final LovedOneService lovedOneService;

	@Override
	public boolean supports(Class<?> clazz) {
		return LovedOneUpdateDto.class.equals(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		LovedOneUpdateDto lovedOneUpdateDto = (LovedOneUpdateDto) target;

		if (lovedOneService.isNameExist(lovedOneUpdateDto)) {
			Object[] errorArgs = new String[] { lovedOneUpdateDto.getName() };
			String defaultMessage = MessageService.getMessage(validationExistName, errorArgs);
			errors.rejectValue(name, validationExistName, errorArgs, defaultMessage);
		}

	}

}
