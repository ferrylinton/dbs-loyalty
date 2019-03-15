package com.dbs.loyalty.web.validator;

import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.dbs.loyalty.config.Constant;
import com.dbs.loyalty.domain.Promo;
import com.dbs.loyalty.service.MessageService;
import com.dbs.loyalty.service.PromoService;

public class PromoValidator implements Validator {

	private final String IMAGE_EMPTY = "validation.notempty.image";

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
		return Promo.class.equals(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		Promo promo = (Promo) target;

		if (promoService.isCodeExist(promo)) {
			Object[] errorArgs = new String[] { promo.getCode() };
			String defaultMessage = MessageService.getMessage(CODE_EXIST, errorArgs);
			errors.rejectValue(CODE, CODE_EXIST, errorArgs, defaultMessage);
		}
		
		if (promoService.isTitleExist(promo)) {
			Object[] errorArgs = new String[] { promo.getCode() };
			String defaultMessage = MessageService.getMessage(TITLE_EXIST, errorArgs);
			errors.rejectValue(TITLE, TITLE_EXIST, errorArgs, defaultMessage);
		}

		if (Constant.ZERO.equals(promo.getId()) && (promo.getFile() == null || promo.getFile().isEmpty())){
			String defaultMessage = MessageService.getMessage(IMAGE_EMPTY);
			errors.rejectValue(FILE, IMAGE_EMPTY, defaultMessage);
        }
		
	}

}
