package com.dbs.loyalty.web.validator;

import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.dbs.loyalty.service.dto.CustomerActivateDto;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class CustomerActivateValidator implements Validator {

	@Override
	public boolean supports(Class<?> clazz) {
		return CustomerActivateDto.class.equals(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		CustomerActivateDto customerActivateDto = (CustomerActivateDto) target;
		
		
		if(customerActivateDto.getPassword() != null && customerActivateDto.getConfirmPassword() != null) {
			if(!customerActivateDto.getPassword().equals(customerActivateDto.getConfirmPassword())) {
				String defaultMessage = "Password is not confirmed";
				errors.rejectValue("confirmPassword", defaultMessage, defaultMessage);
			}
		}
	}

}
