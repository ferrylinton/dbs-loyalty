package com.dbs.loyalty.web.validator;

import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.dbs.loyalty.service.EventService;
import com.dbs.loyalty.service.dto.EventDto;
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
		return EventDto.class.equals(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		EventDto eventDto = (EventDto) target;

		if (eventService.isTitleExist(eventDto)) {
			Object[] errorArgs = new String[] { eventDto.getTitle() };
			String defaultMessage = MessageUtil.getMessage(validationExistTitle, errorArgs);
			errors.rejectValue(title, validationExistTitle, errorArgs, defaultMessage);
		}

		if (eventDto.getId() == null && (eventDto.getImageFile() == null || eventDto.getImageFile().isEmpty())){
			String defaultMessage = MessageUtil.getMessage(validationNotEmptyImageFile);
			errors.rejectValue(imageFile, validationNotEmptyImageFile, defaultMessage);
        }
		
		if (eventDto.getId() == null && (eventDto.getMaterialFile() == null || eventDto.getMaterialFile().isEmpty())){
			String defaultMessage = MessageUtil.getMessage(validationNotEmptyMaterialFile);
			errors.rejectValue(materialFile, validationNotEmptyImageFile, defaultMessage);
        }
		
	}

}
