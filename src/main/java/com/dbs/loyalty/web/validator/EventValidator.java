package com.dbs.loyalty.web.validator;

import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.dbs.loyalty.domain.Event;
import com.dbs.loyalty.service.EventService;
import com.dbs.loyalty.util.MessageUtil;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class EventValidator implements Validator {

	private String validationNotEmptyImageFile = "validation.empty.multipartFileImage";

	private String multipartFileImage = "multipartFileImage";
	
	private String validationNotEmptyMaterialFile = "validation.empty.multipartFilePdf";

	private String multipartFileMaterial = "multipartFileMaterial";

	private String validationExistTitle = "validation.exist.title";

	private String title = "title";

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
			String defaultMessage = MessageUtil.getMessage(validationExistTitle, errorArgs);
			errors.rejectValue(title, validationExistTitle, errorArgs, defaultMessage);
		}

		if (event.getId() == null && event.getMultipartFileImage().isEmpty()){
			String defaultMessage = MessageUtil.getMessage(validationNotEmptyImageFile);
			errors.rejectValue(multipartFileImage, validationNotEmptyImageFile, defaultMessage);
        }
		
		if (event.getId() == null && event.getMultipartFileMaterial().isEmpty()){
			String defaultMessage = MessageUtil.getMessage(validationNotEmptyMaterialFile);
			errors.rejectValue(multipartFileMaterial, validationNotEmptyImageFile, defaultMessage);
        }
		
	}

}
