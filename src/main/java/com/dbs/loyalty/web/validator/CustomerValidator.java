package com.dbs.loyalty.web.validator;

import static com.dbs.loyalty.config.constant.DomainConstant.EMAIL;
import static com.dbs.loyalty.config.constant.DomainConstant.MULTIPART_FILE_IMAGE;
import static com.dbs.loyalty.config.constant.ValidationConstant.VALIDATION_EMPTY_FILE;
import static com.dbs.loyalty.config.constant.ValidationConstant.VALIDATION_EXIST;
import static com.dbs.loyalty.config.constant.ValidationConstant.VALIDATION_FILE_SIZE;
import static com.dbs.loyalty.config.constant.ValidationConstant.VALIDATION_NOT_IMAGE;

import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.dbs.loyalty.config.ApplicationProperties;
import com.dbs.loyalty.domain.Customer;
import com.dbs.loyalty.service.CustomerService;
import com.dbs.loyalty.util.MessageUtil;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class CustomerValidator implements Validator {
	
	private final CustomerService customerService;
	
	private final ApplicationProperties applicationProperties;

	@Override
	public boolean supports(Class<?> clazz) {
		return Customer.class.equals(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		Customer customer = (Customer) target;
		
		if (customerService.isEmailExist(customer.getId(), customer.getEmail())) {
			Object[] errorArgs = new String[] { customer.getEmail() };
			String defaultMessage = MessageUtil.getMessage(VALIDATION_EXIST, errorArgs);
			errors.rejectValue(EMAIL, VALIDATION_EXIST, errorArgs, defaultMessage);
		}
		
		if(customer.getId() == null && customer.getMultipartFileImage().isEmpty()) {
			String defaultMessage = MessageUtil.getMessage(VALIDATION_EMPTY_FILE);
			errors.rejectValue(MULTIPART_FILE_IMAGE, VALIDATION_EMPTY_FILE, defaultMessage);
		}
		
		if(!customer.getMultipartFileImage().isEmpty()) {
			if(customer.getMultipartFileImage().getSize() > applicationProperties.getFile().getImageMaxSize()) {
				long maxSize = applicationProperties.getFile().getImageMaxSize() / (1024 * 1024);
				Object[] errorArgs = new Object[] { maxSize };
				String defaultMessage = MessageUtil.getMessage(VALIDATION_FILE_SIZE, errorArgs);
				errors.rejectValue(MULTIPART_FILE_IMAGE, VALIDATION_FILE_SIZE, errorArgs, defaultMessage);
			}else if(!applicationProperties.getFile().getImageContentTypes().contains(customer.getMultipartFileImage().getContentType())) {
				String defaultMessage = MessageUtil.getMessage(VALIDATION_NOT_IMAGE);
				errors.rejectValue(MULTIPART_FILE_IMAGE, VALIDATION_NOT_IMAGE, defaultMessage);
			}
		}
		
	}

}
