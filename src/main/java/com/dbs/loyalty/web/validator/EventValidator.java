package com.dbs.loyalty.web.validator;

import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.dbs.loyalty.config.constant.DomainConstant;
import com.dbs.loyalty.config.constant.ValidationConstant;
import com.dbs.loyalty.domain.evt.Event;
import com.dbs.loyalty.service.EventService;
import com.dbs.loyalty.util.MessageUtil;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class EventValidator implements Validator {

	private final EventService eventService;

	@Override
	public boolean supports(Class<?> clazz) {
		return Event.class.equals(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		Event event = (Event) target;

		if (eventService.isTitleExist(event.getId(), event.getTitle())) {
			Object[] errorArgs = new String[] { event.getTitle() };
			String defaultMessage = MessageUtil.getMessage(ValidationConstant.VALIDATION_EXIST, errorArgs);
			errors.rejectValue(DomainConstant.TITLE, ValidationConstant.VALIDATION_EXIST, errorArgs, defaultMessage);
		}

		if (event.getId() == null && event.getMultipartFileImage().isEmpty()){
			String defaultMessage = MessageUtil.getMessage(ValidationConstant.VALIDATION_EMPTY_FILE);
			errors.rejectValue(DomainConstant.MULTIPART_FILE_IMAGE, ValidationConstant.VALIDATION_EMPTY_FILE, defaultMessage);
        }
		
		if (event.getId() == null && event.getMultipartFileMaterial().isEmpty()){
			String defaultMessage = MessageUtil.getMessage(ValidationConstant.VALIDATION_EMPTY_FILE);
			errors.rejectValue(DomainConstant.MULTIPART_FILE_MATERIAL, ValidationConstant.VALIDATION_EMPTY_FILE, defaultMessage);
        }
		
	}

}
