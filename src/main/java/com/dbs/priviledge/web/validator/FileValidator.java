package com.dbs.priviledge.web.validator;

import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.dbs.priviledge.config.Constant;
import com.dbs.priviledge.model.File;
import com.dbs.priviledge.service.MessageService;

public class FileValidator implements Validator{
	
	private final String IMAGE_EMPTY = "validation.notempty.image";

	private final String FILE = "file";

	@Override
	public boolean supports(Class<?> clazz) {
		return File.class.equals(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		File file = (File) target;

		if (Constant.ZERO.equals(file.getId()) && file.getFile().isEmpty()){
			String defaultMessage = MessageService.getMessage(IMAGE_EMPTY);
			errors.rejectValue(FILE, IMAGE_EMPTY, defaultMessage);
        }
	}

}
