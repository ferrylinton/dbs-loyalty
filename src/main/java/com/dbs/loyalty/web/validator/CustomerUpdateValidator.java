package com.dbs.loyalty.web.validator;

import java.util.Optional;

import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.dbs.loyalty.service.CustomerService;
import com.dbs.loyalty.service.dto.CustomerDto;
import com.dbs.loyalty.service.dto.CustomerUpdateDto;
import com.dbs.loyalty.util.MessageUtil;
import com.dbs.loyalty.util.SecurityUtil;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class CustomerUpdateValidator implements Validator {
	
	private String validationExistEmail = "validation.exist.email";

	private String email = "email";

	private final CustomerService customerService;

	@Override
	public boolean supports(Class<?> clazz) {
		return CustomerUpdateDto.class.equals(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		CustomerUpdateDto customerUpdateDto = (CustomerUpdateDto) target;
		Optional<CustomerDto> customerDto = customerService.findByEmail(SecurityUtil.getLogged());

		if(customerDto.isPresent()) {
			customerUpdateDto.setId(customerDto.get().getId());
			
			if (customerService.isEmailExist(customerUpdateDto)) {
				Object[] errorArgs = new String[] { customerUpdateDto.getEmail() };
				String defaultMessage = MessageUtil.getMessage(validationExistEmail, errorArgs);
				errors.rejectValue(email, validationExistEmail, errorArgs, defaultMessage);
			}
		}
	}
}
