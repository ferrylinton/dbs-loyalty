package com.dbs.loyalty.web.validator;

import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.dbs.loyalty.service.LovedOneService;
import com.dbs.loyalty.service.MessageService;
import com.dbs.loyalty.service.dto.LovedOneAddDto;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class LovedOneAddValidator implements Validator {

	private String NAME_EXIST = "validation.exist.name";

	private String NAME = "name";

	private final LovedOneService lovedOneService;

	@Override
	public boolean supports(Class<?> clazz) {
		return LovedOneAddDto.class.equals(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		LovedOneAddDto lovedOneAddDto = (LovedOneAddDto) target;

		if (lovedOneService.isNameExist(lovedOneAddDto)) {
			Object[] errorArgs = new String[] { lovedOneAddDto.getName() };
			String defaultMessage = MessageService.getMessage(NAME_EXIST, errorArgs);
			errors.rejectValue(NAME, NAME_EXIST, errorArgs, defaultMessage);
		}

	}

}
