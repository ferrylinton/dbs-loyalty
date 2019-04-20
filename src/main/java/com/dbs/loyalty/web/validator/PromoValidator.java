package com.dbs.loyalty.web.validator;

import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.dbs.loyalty.service.PromoService;
import com.dbs.loyalty.service.dto.PromoDto;
import com.dbs.loyalty.util.MessageUtil;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class PromoValidator implements Validator {

	private String validationNotEmptyImageFile = "validation.notempty.imageFile";

	private String imageFile = "imageFile";
	
	private String validationExistCode = "validation.exist.code";

	private String code = "code";
	
	private String validationExistTitle = "validation.exist.title";

	private String title = "title";

	private final PromoService promoService;

	@Override
	public boolean supports(Class<?> clazz) {
		return PromoDto.class.equals(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		PromoDto promoDto = (PromoDto) target;

		if (promoService.isCodeExist(promoDto)) {
			Object[] errorArgs = new String[] { promoDto.getCode() };
			String defaultMessage = MessageUtil.getMessage(validationExistCode, errorArgs);
			errors.rejectValue(code, validationExistCode, errorArgs, defaultMessage);
		}
		
		if (promoService.isTitleExist(promoDto)) {
			Object[] errorArgs = new String[] { promoDto.getCode() };
			String defaultMessage = MessageUtil.getMessage(validationExistTitle, errorArgs);
			errors.rejectValue(title, validationExistTitle, errorArgs, defaultMessage);
		}

		if (promoDto.getId() == null && (promoDto.getImageFile() == null || promoDto.getImageFile().isEmpty())){
			String defaultMessage = MessageUtil.getMessage(validationNotEmptyImageFile);
			errors.rejectValue(imageFile, validationNotEmptyImageFile, defaultMessage);
        }
		
	}

}
