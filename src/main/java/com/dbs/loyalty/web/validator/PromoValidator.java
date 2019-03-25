package com.dbs.loyalty.web.validator;

import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.dbs.loyalty.service.MessageService;
import com.dbs.loyalty.service.PromoService;
import com.dbs.loyalty.service.dto.PromoDto;

public class PromoValidator implements Validator {

	private final String EMPTY_FILE = "validation.notempty.file";

	private final String FILE = "file";
	
	private final String CODE_EXIST = "validation.exist.code";

	private final String CODE = "code";
	
	private final String TITLE_EXIST = "validation.exist.title";

	private final String TITLE = "title";

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
			String defaultMessage = MessageService.getMessage(CODE_EXIST, errorArgs);
			errors.rejectValue(CODE, CODE_EXIST, errorArgs, defaultMessage);
		}
		
		if (promoService.isTitleExist(promoDto)) {
			Object[] errorArgs = new String[] { promoDto.getCode() };
			String defaultMessage = MessageService.getMessage(TITLE_EXIST, errorArgs);
			errors.rejectValue(TITLE, TITLE_EXIST, errorArgs, defaultMessage);
		}

		if (promoDto.getId() == null && (promoDto.getFile() == null || promoDto.getFile().isEmpty())){
			String defaultMessage = MessageService.getMessage(EMPTY_FILE);
			errors.rejectValue(FILE, EMPTY_FILE, defaultMessage);
        }
		
	}

}
