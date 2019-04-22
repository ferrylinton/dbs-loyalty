package com.dbs.loyalty.web.validator;

import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.dbs.loyalty.service.EventService;
import com.dbs.loyalty.service.dto.EventFormDto;
import com.dbs.loyalty.util.MessageUtil;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class EventValidator implements Validator {

	private String validationNotEmptyImageFile = "validation.notempty.imageFile";

	private String imageFile = "imageFile";
	
	private String validationNotEmptyMaterialFile = "validation.notempty.materialFile";

	private String materialFile = "materialFile";

	private String validationExistTitle = "validation.exist.title";

	private String title = "title";

	private final EventService eventService;

	@Override
	public boolean supports(Class<?> clazz) {
		return EventFormDto.class.equals(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		EventFormDto eventFormDto = (EventFormDto) target;

		if (eventService.isTitleExist(eventFormDto.getId(), eventFormDto.getTitle())) {
			Object[] errorArgs = new String[] { eventFormDto.getTitle() };
			String defaultMessage = MessageUtil.getMessage(validationExistTitle, errorArgs);
			errors.rejectValue(title, validationExistTitle, errorArgs, defaultMessage);
		}

		if (eventFormDto.getId() == null && (eventFormDto.getImageFile() == null || eventFormDto.getImageFile().isEmpty())){
			String defaultMessage = MessageUtil.getMessage(validationNotEmptyImageFile);
			errors.rejectValue(imageFile, validationNotEmptyImageFile, defaultMessage);
        }
		
		if (eventFormDto.getId() == null && (eventFormDto.getMaterialFile() == null || eventFormDto.getMaterialFile().isEmpty())){
			String defaultMessage = MessageUtil.getMessage(validationNotEmptyMaterialFile);
			errors.rejectValue(materialFile, validationNotEmptyImageFile, defaultMessage);
        }
		
	}

}
