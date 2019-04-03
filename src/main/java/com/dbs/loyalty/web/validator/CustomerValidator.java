package com.dbs.loyalty.web.validator;

import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.dbs.loyalty.service.CustomerService;
import com.dbs.loyalty.service.MessageService;
import com.dbs.loyalty.service.dto.CustomerDto;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class CustomerValidator implements Validator {

	private String validationExistEmail = "validation.exist.email";

	private String email = "email";

	private final CustomerService customerService;

	@Override
	public boolean supports(Class<?> clazz) {
		return CustomerDto.class.equals(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		CustomerDto customerDto = (CustomerDto) target;

		if (customerService.isEmailExist(customerDto)) {
			Object[] errorArgs = new String[] { customerDto.getEmail() };
			String defaultMessage = MessageService.getMessage(validationExistEmail, errorArgs);
			errors.rejectValue(email, validationExistEmail, errorArgs, defaultMessage);
		}

	}

}
