package com.dbs.loyalty.web.validator;

import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.dbs.loyalty.domain.Promo;
import com.dbs.loyalty.service.PromoService;
import com.dbs.loyalty.util.MessageUtil;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class PromoValidator implements Validator {

	private String validationEmptyMultipartFileImage = "validation.empty.multipartFileImage";

	private String multipartFileImage = "multipartFileImage";
	
	private String validationExistCode = "validation.exist.code";

	private String code = "code";
	
	private String validationExistTitle = "validation.exist.title";

	private String title = "title";

	private final PromoService promoService;

	@Override
	public boolean supports(Class<?> clazz) {
		return Promo.class.equals(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		Promo promo = (Promo) target;

		if (promoService.isCodeExist(promo.getId(), promo.getCode())) {
			Object[] errorArgs = new String[] { promo.getCode() };
			String defaultMessage = MessageUtil.getMessage(validationExistCode, errorArgs);
			errors.rejectValue(code, validationExistCode, errorArgs, defaultMessage);
		}
		
		if (promoService.isTitleExist(promo.getId(), promo.getTitle())) {
			Object[] errorArgs = new String[] { promo.getCode() };
			String defaultMessage = MessageUtil.getMessage(validationExistTitle, errorArgs);
			errors.rejectValue(title, validationExistTitle, errorArgs, defaultMessage);
		}

		if (promo.getId() == null && promo.getMultipartFileImage().isEmpty()){
			String defaultMessage = MessageUtil.getMessage(validationEmptyMultipartFileImage);
			errors.rejectValue(multipartFileImage, validationEmptyMultipartFileImage, defaultMessage);
        }
		
	}

}
