package com.dbs.loyalty.web.validator;

import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.dbs.loyalty.service.PromoService;
import com.dbs.loyalty.service.dto.PromoDto;
import com.dbs.loyalty.util.MessageUtil;

public class PromoValidator implements Validator {

	private String validationNotEmptyFile = "validation.notempty.file";

	private String file = "file";
	
	private String validationExistCode = "validation.exist.code";

	private String code = "code";
	
	private String validationExistTitle = "validation.exist.title";

	private String title = "title";

	private final PromoService promoService;

	public PromoValidator(final PromoService promoService) {
		this.promoService = promoService;
	}

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

		if (promoDto.getId() == null && (promoDto.getFile() == null || promoDto.getFile().isEmpty())){
			String defaultMessage = MessageUtil.getMessage(validationNotEmptyFile);
			errors.rejectValue(file, validationNotEmptyFile, defaultMessage);
        }
		
	}

}
