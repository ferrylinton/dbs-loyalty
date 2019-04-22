package com.dbs.loyalty.web.validator;

import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.dbs.loyalty.service.CustomerService;
import com.dbs.loyalty.service.dto.CustomerDto;
import com.dbs.loyalty.service.dto.CustomerFormDto;
import com.dbs.loyalty.util.MessageUtil;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class CustomerValidator implements Validator {

	private String validationExistEmail = "validation.exist.email";
	
	private String validationNotEmptyImageFile = "validation.notempty.imageFile";

	private String email = "email";
	
	private String imageFile = "imageFile";

	private final CustomerService customerService;

	@Override
	public boolean supports(Class<?> clazz) {
		return CustomerFormDto.class.equals(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		CustomerFormDto customerFormDto = (CustomerFormDto) target;

		if (customerService.isEmailExist(customerFormDto.getId(), customerFormDto.getEmail())) {
			Object[] errorArgs = new String[] { customerFormDto.getEmail() };
			String defaultMessage = MessageUtil.getMessage(validationExistEmail, errorArgs);
			errors.rejectValue(email, validationExistEmail, errorArgs, defaultMessage);
		}
		
		if(customerFormDto.getId() == null && customerFormDto.getImageFile().isEmpty()) {
			String defaultMessage = MessageUtil.getMessage(validationNotEmptyImageFile);
			errors.rejectValue(imageFile, validationNotEmptyImageFile, defaultMessage);
		}

	}

}
