package com.dbs.loyalty.web.validator;

import java.util.Optional;

import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.dbs.loyalty.service.CustomerService;
import com.dbs.loyalty.service.MessageService;
import com.dbs.loyalty.service.dto.CustomerDto;
import com.dbs.loyalty.service.dto.CustomerUpdateDto;
import com.dbs.loyalty.util.SecurityUtil;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class CustomerUpdateValidator implements Validator {
	
	private String EMAIL_EXIST = "validation.exist.email";

	private String EMAIL = "email";

	private final CustomerService customerService;

	@Override
	public boolean supports(Class<?> clazz) {
		return CustomerUpdateDto.class.equals(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		CustomerUpdateDto customerUpdateDto = (CustomerUpdateDto) target;
		Optional<CustomerDto> customerDto = customerService.findByEmail(SecurityUtil.getLogged());

		customerUpdateDto.setId(customerDto.get().getId());
		if (customerService.isEmailExist(customerUpdateDto)) {
			Object[] errorArgs = new String[] { customerUpdateDto.getEmail() };
			String defaultMessage = MessageService.getMessage(EMAIL_EXIST, errorArgs);
			errors.rejectValue(EMAIL, EMAIL_EXIST, errorArgs, defaultMessage);
		}

	}
}
